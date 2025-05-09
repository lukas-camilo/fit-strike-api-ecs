package com.lucascamilo.fit_strike_api.infrastructure.dynamodb;

import com.lucascamilo.fit_strike_api.domain.User;
import com.lucascamilo.fit_strike_api.domain.port.UserRepository;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryDynamoDB implements UserRepository {
    private final DynamoDbEnhancedClient dynamoDbClient;
    private final DynamoDbTable<User> userTable;

    public UserRepositoryDynamoDB(DynamoDbEnhancedClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
        this.userTable = dynamoDbClient.table("users", TableSchema.fromBean(User.class));
    }

    @Override
    public User save(User user) {
        userTable.putItem(user);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userTable.getItem(Key.builder().partitionValue(id).build()));
    }

    @Override
    public List<User> findAll() {
        return userTable.scan().items().stream().collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        userTable.deleteItem(Key.builder().partitionValue(id).build());
    }
}