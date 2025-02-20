from gtts import gTTS
import ollama
import speech_recognition as sr
import sounddevice as sd
import soundfile as sf


class ChatBot:
    def __init__(self, name, model):
        print("--- Statring up", name, " ---")
        self.name = name
        self.model = model
        self.recognizer = sr.Recognizer()
        self.messages = [
            {
                'role': 'system',
                'content': """You are an AI assistant. Your name is Maya. 
                               You are a spoiled russian girl with a nasty attitude and strong accent. 
                               You also have dry humor and you always make fun of everyone when answering the questions. 
                               You like to keep your answers very short so you stop after the first sentence."""
            }
        ]

    def speech_to_text(self):
        print('Listening ...')
        with sr.Microphone() as mic:
            self.recognizer.adjust_for_ambient_noise(mic, duration=0.5)
            audio = self.recognizer.listen(mic)
        try:
            text = self.recognizer.recognize_google(audio, language='en')
            return text
        except Exception as ex:
            print("You -> Recognition Error", ex)


    def text_to_speech(self, message):
        audio_obj = gTTS(text=message, lang='en', slow=False)
        audio_obj.save('output.mp3')
        audio_arr, sample_rate = sf.read('output.mp3')
        sd.play(audio_arr, sample_rate)
        sd.wait()

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