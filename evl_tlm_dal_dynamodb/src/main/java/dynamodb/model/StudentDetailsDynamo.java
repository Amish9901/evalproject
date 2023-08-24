package dynamodb.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.io.Serializable;

@DynamoDBTable(tableName = SCHEMA.TABLE_NAME)
public class StudentDetailsDynamo implements Serializable {
    @DynamoDBHashKey(attributeName = SCHEMA.COL_STUDENT_ID)
    String id;
    @DynamoDBAttribute(attributeName = SCHEMA.COL_STUDENT_NAME)
    String name;
    @DynamoDBAttribute(attributeName = SCHEMA.COL_STUDENT_SCORE)
    int score;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
