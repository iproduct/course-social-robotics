import ollama
from gtts import gTTS
import soundfile as sf
import sounddevice as sd
import speech_recognition as sr

class ChatBot:
    def __init__(self, name, model):
        self.name = name
        self.model = model
        self.recognizer = sr.Recognizer()
        self.messages = [
            {
                'role':'system',
                'content': f"""You are an AI assistant. Your name is {name}. 
                   You are a spoiled russian girl with a nasty attitude and strong accent. 
                   You also have dry humor and you always make fun of everyone when answering the questions. 
                   You like to keep your answers very short so you stop after the first sentence."""
            }
        ]

    def text_to_speech(self, text):
        audio = gTTS(text=text, lang='en', slow=False)
        audio.save(f'{self.name}.mp3')
        audio_arr, sample_rate = sf.read(f'{self.name}.mp3')
        sd.play(audio_arr, sample_rate)
        sd.wait()

    def speech_to_text(self):
        print(f'{self.name} is listening ...')
        with sr.Microphone() as source:
            self.recognizer.adjust_for_ambient_noise(source)
            audio = self.recognizer.listen(source)
            try:
                text = self.recognizer.recognize_google(audio, language='en')
                return text
            except sr.UnknownValueError as ex:
                ...
            except Exception as ex:
                print(ex)

    def is_quit_command(self, message):
        lst = ['quit', 'exit', 'stop', 'finish', 'bye', 'бай-бай']
        return True if any(sw in message.lower() for sw in lst) else False

    def run(self):
        # self.text_to_speech("Hello World")
        # message = self.speech_to_text()
        # print(f'Message: {message}')
        while True:
            message = self.speech_to_text()
            if not message:
                continue
            print(f'U: {message}')
            self.messages.append({
                'role':'user',
                'content': f"""{message}"""
            })
            resp = ollama.chat(model=self.model, messages=self.messages)
            resp_message = resp.message.content
            print(f'{self.name}:{resp_message}')
            self.text_to_speech(resp_message)
            self.messages.append({
                'role':'system',
                'content': f"""{resp_message}"""
            })
            if self.is_quit_command(message):
                break


if __name__ == '__main__':
    # ChatBot demo
    maya = ChatBot("Maya", model='llama3.2:latest')
    maya.run()
