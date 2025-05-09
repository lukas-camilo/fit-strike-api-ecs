package com.lucascamilo.fit_strike_api.infrastructure.dynamodb;

import com.lucascamilo.fit_strike_api.domain.User;
import com.lucascamilo.fit_strike_api.domain.port.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryDynamoDB implements UserRepository {

    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "users";

    @Autowired
    public UserRepositoryDynamoDB(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    @Override
    public User save(User user) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(user.getId()).build());
        item.put("name", AttributeValue.builder().s(user.getName()).build());
        item.put("email", AttributeValue.builder().s(user.getEmail()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build();

        dynamoDbClient.putItem(request);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();
        if (item == null || item.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapToUser(item));
    }

    @Override
    public List<User> findAll() {
        ScanRequest request = ScanRequest.builder()
                .tableName(tableName)
                .build();

        List<Map<String, AttributeValue>> items = dynamoDbClient.scan(request).items();
        return items.stream()
                .map(this::mapToUser)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(id).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        dynamoDbClient.deleteItem(request);
    }

    private User mapToUser(Map<String, AttributeValue> item) {
        return new User(
                item.get("id").s(),
                item.get("name").s(),
                item.get("email").s()
        );
    }
}