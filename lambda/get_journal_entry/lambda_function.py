import json
import boto3
from boto3.dynamodb.conditions import Key

TABLE_NAME = "mood_journal"
dynamodb = boto3.resource("dynamodb")
table = dynamodb.Table(TABLE_NAME)

def lambda_handler(event, context):
    try:
        # get user ID from the Lambda Authorizer
        user_id = event["requestContext"]["authorizer"]["sub"]

        # query the table with reverse chronological order
        response = table.query(
            KeyConditionExpression=Key("user_id").eq(user_id),
            ScanIndexForward=False
        )

        items = response.get("Items", [])

        return {
            "statusCode": 200,
            "body": json.dumps({"entries": items})
        }

    except Exception as e:
        print(f"Error: {e}")
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }