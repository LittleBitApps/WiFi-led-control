from flask import Flask, request
app = Flask(__name__)



@app.route('/color', methods=["POST"])
def post_request():
    received = request.form
    response = "Server received %s" %received
    print(response)
    return response
if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)
