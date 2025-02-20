class ChatBot:
    def __init__(self, name, model):
        print("--- Statring up ", name, " ---")
        self.name = name
        self.model = model

    def run(self):
        ollama.chat(self.name, self.model)

