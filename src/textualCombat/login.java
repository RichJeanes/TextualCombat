package textualCombat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.dynamodbv2.model.UpdateItemResult;
import com.amazonaws.services.dynamodbv2.util.Tables;

public class login {

    static AmazonDynamoDBClient dynamoDB;
    String table_name = "users";
    String table_char = "char";

    public login() {
        dynamoDB = new AmazonDynamoDBClient(new BasicAWSCredentials("AKIAJ5SII452HJCXONUA", "sWfx902vRvsr/2C0p9ORQE4yMnCg69lQjBJ+p1Gh"));
        Region usWest2 = Region.getRegion(Regions.US_WEST_2);
        dynamoDB.setRegion(usWest2);
        return;
    }

    public boolean create_user_db() {
        // Create table if it does not exist yet
        if (Tables.doesTableExist(dynamoDB, table_name)) {
            System.out.println("Table " + table_name + " is already ACTIVE");
        } else {
            // Create a table with a primary hash key named 'name', which holds a string
            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(table_name)
                    .withKeySchema(new KeySchemaElement().withAttributeName("user_name").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("user_name").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
            TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
            System.out.println("Created Table: " + createdTableDescription);

            // Wait for it to become active
            System.out.println("Waiting for " + table_name + " to become ACTIVE...");
            Tables.waitForTableToBecomeActive(dynamoDB, table_name);
        }
        // Describe our new table
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(table_name);
        TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        return true;
    }

    public boolean create_char_db() {
        // Create table if it does not exist yet
        if (Tables.doesTableExist(dynamoDB, table_char)) {
            System.out.println("Table " + table_char + " is already ACTIVE");
        } else {
            // Create a table with a primary hash key named 'name', which holds a string
            CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(table_char)
                    .withKeySchema(new KeySchemaElement().withAttributeName("char_name").withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition().withAttributeName("char_name").withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
            TableDescription createdTableDescription = dynamoDB.createTable(createTableRequest).getTableDescription();
            System.out.println("Created Table: " + createdTableDescription);

            // Wait for it to become active
            System.out.println("Waiting for " + table_char + " to become ACTIVE...");
            Tables.waitForTableToBecomeActive(dynamoDB, table_char);
        }
        // Describe our new table
        DescribeTableRequest describeTableRequest = new DescribeTableRequest().withTableName(table_char);
        TableDescription tableDescription = dynamoDB.describeTable(describeTableRequest).getTable();
        System.out.println("Table Description: " + tableDescription);
        return true;
    }

    public boolean add_user(Map<String, AttributeValue> item) {
        if (does_user_exist(item.get("user_name").getS())) {
            return false;
        }
        PutItemRequest putItemRequest = new PutItemRequest(table_name, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        return true;
    }

    public boolean add_char(Map<String, AttributeValue> item) {
        PutItemRequest putItemRequest = new PutItemRequest(table_char, item);
        PutItemResult putItemResult = dynamoDB.putItem(putItemRequest);
        System.out.println("Result: " + putItemResult);
        return true;
    }

    public boolean check_user_and_password(String user_name, String user_password) {
        if( user_password==null|| user_password.equals("")) return false;
    	HashMap<String, Condition> scanFilter = new HashMap<String, Condition>();
        Condition condition = new Condition()
                .withComparisonOperator(ComparisonOperator.EQ.toString())
                .withAttributeValueList(new AttributeValue().withS(user_name));
        scanFilter.put("user_name", condition);
        ScanRequest scanRequest = new ScanRequest(table_name).withScanFilter(scanFilter);
        ScanResult scanResult = dynamoDB.scan(scanRequest);
        String temp = scanResult.toString();
        List<Map<String, AttributeValue>> map = scanResult.getItems();
        if (temp.contains(user_password)) {
            System.out.println("Password good!");
            return true;
        } else {
            System.out.println("Password bad!");
            return false;
        }
    }

    public Map<String, AttributeValue> find_user(String user_name) {
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("user_name", new AttributeValue().withS(user_name));

        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(table_name)
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> map = result.getItem();
        //System.out.println("Yar: " + map.get("user_name").getS());
        return map;
    }

    public Map<String, AttributeValue> find_char(String char_name) {
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("char_name", new AttributeValue().withS(char_name));

        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(table_char)
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> map = result.getItem();
        System.out.println("Yar: " + map.toString());
        return map;
    }

    public String find_char_name(String user_name) {
        String output;
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("char_name", new AttributeValue().withS(user_name));

        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(table_char)
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> map = result.getItem();
        output = map.get("user_name").getS();
        return output;
    }

    public void update_char(String char_name, String user_name, int health, int attack, int defence, int strength, int agility, int wins) {
        Map<String, AttributeValueUpdate> updateItems = new HashMap<String, AttributeValueUpdate>();

        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("char_name", new AttributeValue().withS(char_name));

        updateItems.put("user_name", new AttributeValueUpdate()
                .withValue(new AttributeValue().withS(user_name)));
        updateItems.put("health", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(health))));
        updateItems.put("attack", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(attack))));
        updateItems.put("defence", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(defence))));
        updateItems.put("strength", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(strength))));
        updateItems.put("agility", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(agility))));
        updateItems.put("wins", new AttributeValueUpdate()
                .withValue(new AttributeValue().withN(Integer.toString(wins))));

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(table_char)
                .withKey(key).withReturnValues(ReturnValue.UPDATED_NEW)
                .withAttributeUpdates(updateItems);

        UpdateItemResult result = dynamoDB.updateItem(updateItemRequest);
        return;
    }

    public boolean does_user_exist(String user_name) {
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("user_name", new AttributeValue().withS(user_name));

        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(table_name)
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> map = result.getItem();
        if (map == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean does_char_exist(String user_name) {
        HashMap<String, AttributeValue> key = new HashMap<String, AttributeValue>();
        key.put("char_name", new AttributeValue().withS(user_name));

        GetItemRequest getItemRequest = new GetItemRequest()
                .withTableName(table_char)
                .withKey(key);

        GetItemResult result = dynamoDB.getItem(getItemRequest);
        Map<String, AttributeValue> map = result.getItem();
        if (map == null) {
            return false;
        } else {
            return true;
        }
    }

    public Map<String, AttributeValue> new_user_item(String user_name, String password) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("user_name", new AttributeValue(user_name));
        item.put("password", new AttributeValue(password));
        System.out.println(item.toString());
        return item;
    }

    public Map<String, AttributeValue> new_char_item(String char_name, String user_name, int health, int attack, int defence, int strength, int agility, int wins) {
        Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
        item.put("char_name", new AttributeValue(char_name));
        item.put("user_name", new AttributeValue(user_name));
        item.put("health", new AttributeValue().withN(Integer.toString(health)));
        item.put("attack", new AttributeValue().withN(Integer.toString(attack)));
        item.put("defence", new AttributeValue().withN(Integer.toString(defence)));
        item.put("strength", new AttributeValue().withN(Integer.toString(strength)));
        item.put("agility", new AttributeValue().withN(Integer.toString(agility)));
        item.put("wins", new AttributeValue().withN(Integer.toString(wins)));
        System.out.println(item.toString());
        return item;
    }

}
