import json
import base64

def lambda_handler(event, context):
    auth_header = event.get('authorizationToken', '')

    if not auth_header.startswith('Bearer '):
        raise Exception('Unauthorized')

    token = auth_header.split(' ')[1]

    try:
        # Decode the JWT (header.payload.signature)
        payload_segment = token.split('.')[1]
        padded = payload_segment + '=' * (-len(payload_segment) % 4)  # fix padding
        decoded_bytes = base64.urlsafe_b64decode(padded)
        payload = json.loads(decoded_bytes)

        # Optional: basic checks
        if payload.get("token_use") != "access":
            raise Exception("Invalid token type")
        if "sub" not in payload:
            raise Exception("Missing sub claim")

        principal_id = payload["sub"]
        return generate_policy(principal_id, "Allow", event["methodArn"], {
            "username": payload.get("username", ""),
            "sub": principal_id
        })

    except Exception as e:
        print(f"Token decode error: {e}")
        raise Exception("Unauthorized")


def generate_policy(principal_id, effect, resource, context=None):
    policy = {
        "principalId": principal_id,
        "policyDocument": {
            "Version": "2012-10-17",
            "Statement": [{
                "Action": "execute-api:Invoke",
                "Effect": effect,
                "Resource": resource
            }]
        }
    }
    if context:
        policy["context"] = context
    return policy