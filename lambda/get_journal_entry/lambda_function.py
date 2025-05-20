import json
import boto3
import os
import time
from decimal import Decimal

# dynamoDB table name
TABLE_NAME = "mood_journal"

# set up DynamoDB resource and table
dynamodb = boto3.resource("dynamodb")
table = dynamodb.Table(TABLE_NAME)


# custom JSON encoder to handle decimal
class DecimalEncoder(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, Decimal):
            return int(obj) if obj % 1 == 0 else float(obj)
        return super(DecimalEncoder, self).default(obj)


def lambda_handler(event, context):
    try:
        # extract user ID from request context
        user_id = event["requestContext"]["authorizer"]["sub"]

        # query DynamoDB for all entries from this user
        response = table.query(
            KeyConditionExpression=boto3.dynamodb.conditions.Key("user_id").eq(user_id)
        )

        items = response.get("Items", [])

        # sort items from newest to oldest by timestamp
        sorted_items = sorted(items, key=lambda x: x["timestamp"], reverse=True)

        return {
            "statusCode": 200,
            "body": json.dumps({"entries": sorted_items}, cls=DecimalEncoder)
        }

    except Exception as e:
        print("Error:", str(e))
        return {
            "statusCode": 500,
            "body": json.dumps({"error": str(e)})
        }