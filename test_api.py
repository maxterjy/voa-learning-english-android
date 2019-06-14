import requests

r = requests.get('https://learningenglish.voanews.com/podcasts')
# r = requests.get('https://learningenglish.voanews.com/rssfeeds')

print(r.text)