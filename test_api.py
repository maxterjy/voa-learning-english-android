import requests

r = requests.get('https://learningenglish.voanews.com/podcasts')

print(r.text)