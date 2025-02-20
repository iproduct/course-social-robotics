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
        while True:
            text = self.speech_to_text()  # input('>')
            if text is not None:
                print(text)
                self.messages.append({
                    'role': 'user',
                    'content': text
                })
                resp = ollama.chat(model=self.model, messages=self.messages)
                text = resp['message']['content']
                print('AI -> ', text)
                first_sentence = text
                self.messages.append({
                    'role': 'system',
                    'content': first_sentence
                })
                self.text_to_speech(first_sentence)
            else:
                break


if __name__ == '__main__':
    maya = ChatBot("Maya", 'llama3.2')
    maya.run()