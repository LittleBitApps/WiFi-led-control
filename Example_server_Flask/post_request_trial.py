import requests

r = requests.post("http://localhost:5000/color", data={'color': "ff45ff"})
print(r.status_code, r.reason)
print(r.text[:300] + '...')
