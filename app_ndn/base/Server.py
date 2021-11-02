import socket
import socketserver
import pickle


class TCPSocketHandler(socketserver.BaseRequestHandler):
    def handle(self):
        print("Server: request received")
        full_msg = b''
        while True:
            msg = self.request.recv(8).strip()
            if len(msg) <= 0:
                break
            full_msg += msg
            try:
                isEnd = pickle.loads(full_msg)
                break
            except Exception as e:
                pass

        request_data = pickle.loads(full_msg)
        print("Server: request response = {}".format(request_data))
        response_data = pickle.dumps(request_data)
        self.request.sendall(response_data)
        self.request.close()


if __name__ == "__main__":
    # HOST, PORT = socket.gethostname(), 9999
    HOST, PORT = socket.gethostname(), 9999

    with socketserver.TCPServer((HOST, PORT), TCPSocketHandler) as server:
        server.serve_forever()
