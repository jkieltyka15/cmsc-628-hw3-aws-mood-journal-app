import json
import time
import boto3
import os

# name of DynamoDB table
TABLE_NAME = "mood_journal"

dynamodb = boto3.resource("dynamodb")
table = dynamodb.Table(TABLE_NAME)

def lambda_handler(event, context):
    try:
        # get user_id from the Lambda Authorizer context
        user_id = event["requestContext"]["authorizer"]["sub"]

        # parse request body
        body = json.loads(event.get("body", "{}"))
        mood = body.get("mood")
        notes = body.get("notes", "")  # optional

        # validate input
        if not mood:
            return {
                "statusCode": 400,
                "body": json.dumps({"error": "Missing required field: mood"})
            }

        # add timestamp
        timestamp = int(time.time())

        # build the item to store
        item = {
            "user_id": user_id,
            "timestamp": timestamp,
            "mood": mood
        }

        if notes:
            item["notes"] = notes

        # put item into DynamoDB
        table.put_item(Item=item)

        return {
            "statusCode": 200,
            "body": json.dumps({"message": "Entry submitted successfully"})
        }

    except Exception as e:
        print(f"Error: {e}")
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }