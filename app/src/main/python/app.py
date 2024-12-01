import requests
import json
import sseclient
def hello_world():
    return "Hello World"

def set_text(txt):
    return txt

def run_dify_workflow(api_key, workflow_inputs, user_id, response_mode='streaming'):
    url = 'https://api.dify.ai/v1/chat-messages'
    headers = {
        'Authorization': f'Bearer {api_key}',
        'Content-Type': 'application/json'
    }

    payload = {
        'query': workflow_inputs,
        'response_mode': response_mode,
        'user': user_id,
        'inputs': workflow_inputs,
    }
    
    if response_mode == 'blocking':
        response = requests.post(url, headers=headers, json=payload)
        import sys
        print(f"res:{response.text}", file=sys.stderr)
        return response.json()
    elif response_mode == 'streaming':
        response = requests.post(url, headers=headers, json=payload, stream=True)
        client = sseclient.SSEClient(response)
        return client.events()

def print_human_readable(data):
    if isinstance(data, dict):
        for key, value in data.items():
            if isinstance(value, str):
                print(f"{key}: {value}")
            elif isinstance(value, dict):
                print(f"{key}:")
                print_human_readable(value)
            else:
                print(f"{key}: {value}")
    elif isinstance(data, str):
        print(data)
    else:
        return json.dumps(data, ensure_ascii=False, indent=2)

def main_work(workflow_input):
    api_key='app-wAScjv4eWxqoUmpMTAZvWQli'
    workflow_inputs = {'talk': workflow_input}
    user_id='user123'
    result = run_dify_workflow(api_key, workflow_inputs, user_id, 'blocking')
    print(f"data:{result.answer.decode('unicode-escape')}", file=sys.stderr)
    return print_human_readable(result)