import ollama


class ChatBot:
    def __init__(self, name, model):
        print("--- Statring up", name, " ---")
        self.name = name
        self.model = model

    def run(self):
        resp = ollama.chat(self.model, messages=[
            {
                'role': 'system',
                'content': 'You are an AI assistant',
            },
            {
                'role': 'user',
                'content': 'Why the sky is blue?',
            }
        ])
        text = resp['message']['content']
        print(text)

if __name__ == '__main__':
    maya = ChatBot("Maya", 'llama3.2')
    maya.run()