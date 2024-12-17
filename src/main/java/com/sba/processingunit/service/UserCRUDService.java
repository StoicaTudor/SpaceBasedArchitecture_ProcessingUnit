package com.sba.processingunit.service;

import com.sba.processingunit.data.User;
import com.sba.processingunit.egress_communication.fire_and_forget.CreateUserFireAndForgetCommand;
import com.sba.processingunit.egress_communication.fire_and_forget.DeleteUserFireAndForgetCommand;
import com.sba.processingunit.egress_communication.fire_and_forget.UpdateUserFireAndForgetCommand;
import com.sba.processingunit.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserCRUDService {
    private final UserRepository userRepository;
    private final DataWriterSupplier dataWriterSupplier;
    private final DataReaderSupplier dataReaderSupplier;

    public UserCRUDService(
            UserRepository userRepository,
            DataWriterSupplier dataWriterSupplier,
            DataReaderSupplier dataReaderSupplier) {
        this.userRepository = userRepository;
        this.dataWriterSupplier = dataWriterSupplier;
        this.dataReaderSupplier = dataReaderSupplier;
    }

    /**
     * Simply return the cache contents.
     * Synchronizing the DB with the Cache is being done in the background -> CronJobs.
     */
    public List<User> getAllUsers() {
//        dataReaderSupplier.sendCommand(new GetAllUserCommand());
        return userRepository.getAllUsers();
    }

    public User getUserByID(String id) {
        return userRepository.getUserByID(id);
    }

    public void updateUser(User user) {
        dataWriterSupplier.sendCommand(new UpdateUserFireAndForgetCommand(user));
        userRepository.updateUser(user);
    }

    public User createUser(User user) {
        // Send to kafka or cache first? :D
        User userWithID = user.setID(UUID.randomUUID().toString());
        dataWriterSupplier.sendCommand(new CreateUserFireAndForgetCommand(userWithID));
        return userRepository.createUser(userWithID);
    }

    public void deleteUser(String id) {
        dataWriterSupplier.sendCommand(new DeleteUserFireAndForgetCommand(new User(id, "", 0)));
        userRepository.deleteUser(id);
    }
}
/*
If you're using **Hazelcast** for your space-based architecture, this changes some of the details and provides you with additional tools to manage cache coherency, consistency, and distribution. Hazelcast is an in-memory data grid (IMDG) that allows you to store, manage, and process data in a distributed fashion, meaning data can be cached in a cluster across multiple nodes.

Here’s how Hazelcast handles caching and data retrieval, and how you can ensure that all users are available in the cache when querying with `getAllUsers()`.

### 1. **Understanding Hazelcast's Caching Model**
   - **Distributed Cache**: Hazelcast stores data in a distributed cache, which is spread across multiple nodes in a cluster. If one node fails, another node can take over, ensuring high availability.
   - **Eviction Policy**: Hazelcast provides configurable eviction policies, such as **Least Recently Used (LRU)**, **Least Frequently Used (LFU)**, and **Time-to-Live (TTL)** for cache entries. You need to ensure that your eviction settings align with your requirements for retaining all users in the cache.
   - **Near Cache**: Hazelcast also has a **near cache** feature, which stores frequently accessed data on the client side to minimize network round trips. However, data might not be synchronized with the cluster cache, so this approach should be used carefully in distributed systems to ensure consistency.

### 2. **How Hazelcast Handles `getAllUsers()` Queries**
   When you query for **all users** in Hazelcast (e.g., `getAllUsers()`), the following may happen:

   - **Cache Hit**: If the cache contains all the users you need (or a sufficient number), it will return the data from the **distributed cache**. The distributed nature of Hazelcast ensures that the data might be stored in different partitions across the cluster, so you are accessing it from the cluster itself.
   - **Cache Miss**: If the data is not found in the cache, Hazelcast will:
     - **Load from the database** (if you have a **write-through** or **read-through** cache configured).
     - **Populate the cache** with the missing data (so future queries are faster).

     In Hazelcast, the system can automatically **load data into the cache** if you have a **CacheLoader** configured to fetch data from a database when the data is missing in the cache.

### 3. **How to Ensure All Users Are in the Cache**
If you're querying `getAllUsers()`, there’s no guarantee that all users are currently in the cache unless you take specific steps to ensure this. Here are some options for ensuring that all users are cached:

#### 1. **Preloading the Cache (Eager Loading)**
   - **Preload all users** into the cache at system startup or periodically. This can be done by iterating over your database and explicitly putting all user data into the Hazelcast cache.
   - This approach makes sure that the cache contains all users, so when you query `getAllUsers()`, the data will already be there.

   ```java
   HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();
   IMap<String, User> userCache = hazelcastInstance.getMap("users");

   // Populate the cache with all users (e.g., from the database)
   List<User> allUsers = databaseService.getAllUsers(); // Assuming this fetches all users from DB
   for (User user : allUsers) {
       userCache.put(user.getId(), user);
   }
   ```

#### 2. **CacheLoader (Read-Through Cache)**
   - **Read-through caching** can automatically load data into the cache when it’s requested and not found. You can implement a `CacheLoader` to load missing users from the database when you query for them.

   Here’s how you can configure a `CacheLoader` with Hazelcast:

   ```java
   public class UserCacheLoader implements CacheLoader<String, User> {
       @Override
       public User load(String key) {
           return databaseService.getUserById(key);  // Load from DB
       }

       @Override
       public Map<String, User> loadAll(Iterable<String> keys) {
           return databaseService.getUsersByIds(keys);  // Load multiple users at once
       }
   }

   // Configuring the cache with a CacheLoader
   Config config = new Config();
   MapConfig mapConfig = new MapConfig("users");
   mapConfig.setCacheLoaderFactoryConfig(new CacheLoaderFactoryConfig()
       .setImplementation(new UserCacheLoader()));
   config.addMapConfig(mapConfig);

   HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
   IMap<String, User> userCache = hazelcastInstance.getMap("users");
   ```

   In this case, if a user is not in the cache, Hazelcast will use the `CacheLoader` to fetch it from the database.

#### 3. **Cache Invalidations or Refresh Strategy**
   - **Periodic Cache Refresh**: If users are updated or added frequently, you may want to implement a **cache refresh** strategy where the cache is periodically populated with new data from the database, ensuring that the cache stays up-to-date.
   - **Event-driven Updates**: You can also use **event listeners** in Hazelcast to invalidate or refresh cache entries when changes are made to the underlying database. For example, if a new user is added, you can immediately insert them into the cache.

#### 4. **Distributed Queries**
   If you're querying for `getAllUsers()`, Hazelcast provides support for **distributed queries** that can access data across the entire cluster, ensuring you get a complete result even if the data is partitioned across different nodes in the cluster. The query will span all Hazelcast nodes and return the data.

   ```java
   SqlQuery<String, User> query = new SqlQuery<String, User>("SELECT * FROM users");
   QueryResultSet<User> resultSet = userCache.query(query);

   for (User user : resultSet) {
       System.out.println(user);
   }
   ```

   This is particularly useful when you have large datasets and want to ensure you're accessing all the data from across the cluster.

### 4. **Consistent Hashing and Partitioning**
   Hazelcast uses a partitioned data model to distribute data across nodes in the cluster. Each partition holds a subset of the data. In a large cluster, a query for `getAllUsers()` will involve accessing multiple partitions, which ensures that data from all nodes is considered.

   Hazelcast automatically handles partitioning and data distribution, so your `getAllUsers()` query can span the entire cluster.

### 5. **Near Cache**
   If your application has frequent access to a small set of users (e.g., a user profile or a list of recent users), you can use **near cache** for better performance. This stores a copy of the cache locally on the client to reduce network latency. However, this is generally not suitable for large-scale queries like `getAllUsers()`, as the near cache is typically used for frequent reads of a smaller subset of data.

### Summary
In Hazelcast, if you want to ensure that all users are in the cache when querying `getAllUsers()`, you can:
- Pre-load all users into the cache at startup.
- Use a **CacheLoader** for a read-through cache pattern, which loads missing users from the database.
- Use **distributed queries** to ensure you're retrieving all users from the entire cluster.
- Leverage **near cache** for frequently accessed users.
- Implement cache invalidation or periodic refresh strategies to keep the cache up-to-date.

Hazelcast provides a variety of tools and strategies to ensure data is available in the cache and up-to-date, but you need to design the caching and data retrieval strategy based on your system’s specific needs, such as cache size, eviction policies, and data access patterns.
 */
// ----------------------------------------
/*
In a space-based architecture, when you're asking for **all users** (e.g., through a query like `getAllUsers()`), the main concern is whether the **cache** contains **all** of the user data. Since caches typically store a subset of data to improve performance, you cannot always be sure that the cache holds all users, especially if the cache is limited in size or if certain users have been evicted from the cache due to expiration policies.

To determine whether the cache contains all users, you need to consider the following:

### 1. **Cache Size and Eviction Policy**
   - **Eviction policy**: If the cache is using an eviction policy (e.g., **LRU** – Least Recently Used), it might remove older or less frequently accessed user data. If the eviction policy has removed data you need, you may not find all users in the cache.
   - **Size limitations**: If the cache has a limited size (due to memory or storage constraints), it might not be able to hold all users, especially if the number of users is large.

### 2. **Cache Population and Synchronization**
   - **Write-through**: In some systems, data is written to both the cache and the database simultaneously when a change occurs. This ensures that the cache and database are synchronized. However, if the cache is only updated based on user access (e.g., lazy loading), some users might not be in the cache.
   - **Cache expiration**: If the cache uses expiration (TTL – Time-to-Live), data might be evicted after a certain period. In this case, you may not be sure that all users are in the cache unless you manage cache refresh policies carefully.
   - **Population strategy**: In a **read-through** cache, if a user is not found in the cache, it will be loaded from the database. Similarly, a **write-behind** cache might delay data writes, so data may not be fully available in the cache in real-time.

### 3. **Cache Invalidation or Refresh Strategy**
   - If your system includes a **cache refresh** mechanism or some form of periodic **cache invalidation**, you can ensure that data stays current in the cache. However, if the refresh hasn’t occurred yet, the cache may not contain all the data.
   - **Event-driven updates**: Some systems use event-based approaches to refresh the cache. If such events haven't been triggered for certain users, they might not be in the cache.

### 4. **What Happens When You Query `getAllUsers()`**
   When you query `getAllUsers()`, the system needs to handle cache misses:

   - **Cache hit**: If all users are in the cache, the query will return quickly with all the data.
   - **Cache miss**: If the cache does not contain all the users (or any), the system will either:
     - Query the database directly for the missing data.
     - Or, if some users are present, the system will retrieve the missing data from the database and update the cache.

   **If the cache doesn’t contain all users**, your architecture should ensure that it handles this case by querying the database and populating the cache with missing data.

### 5. **How to Ensure All Users Are in the Cache**
   - **Pre-loading**: If you need to ensure that all users are in the cache, you might employ a strategy where all user data is **pre-loaded** into the cache on startup or after certain intervals.
   - **Cache warm-up**: For large-scale systems, you could implement a process to **warm-up the cache**, i.e., load a predefined set of users (e.g., the most active ones) into the cache on system initialization or periodically.
   - **Lazy Loading + Cache Population**: Alternatively, you could use **lazy loading** for the first few users and gradually populate the cache as the users are requested. However, this might lead to slower queries initially.

### 6. **Cache Coherency and Data Integrity**
   - You must have a strategy in place to **ensure cache coherency** with the database. If your application is highly dynamic (users are frequently added, updated, or deleted), you might need to implement mechanisms like **cache invalidation** or **cache refresh** to make sure the cache doesn't get stale.

### 7. **Fallback to Database Query**
   - If you're not sure that the cache contains all users, you may want to query the **database** directly for the full list of users. After retrieving the data from the database, you can populate the cache to ensure that subsequent queries benefit from cached data.

   In this case, the cache acts as an optimization layer, and the database is always treated as the authoritative source of truth.

### Summary
To know whether the cache contains **all users** when querying `getAllUsers()`:
- **Check the cache policy**: Understand your cache size, eviction policy, expiration settings, and how data is populated in the cache.
- **Cache miss handling**: When a cache miss occurs, the system should either query the database directly or populate the cache with missing data.
- **Pre-load or warm-up**: If necessary, implement pre-loading or cache warm-up strategies to ensure that all users are cached.
- **Database fallback**: If you cannot be sure, always fall back to the database for a complete list of users and update the cache afterward.

By using a combination of cache management, pre-loading, and fallbacks, you can ensure that your application handles both the cache and database appropriately when querying all users.
 */