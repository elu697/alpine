import socket
import pickle


class TCPSocketSender():
    def __init__(self, host, port, data=None):
        with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as sock:
            sock.close
            sock.connect((host, port))

            request_data = b''
            if data is not None:
                request_data = pickle.dumps(data)
            sock.send(request_data)
            print("Client: request send = {}".format(request_data))

            full_msg = b''
            while True:
                msg = sock.recv(256)
                if len(msg) <= 0:
                    break
                full_msg += msg
                try:
                    isEnd = pickle.loads(full_msg)
                    break
                except Exception as e:
                    pass
            self.receive_data = pickle.loads(full_msg)
            sock.close()
            print("Client: response received = {}".format(self.receive_data))


if __name__ == "__main__":
    HOST, PORT = socket.gethostname(), 9999
    json = {"data": "hello"}
    sender = TCPSocketSender(HOST, PORT, json)
    response = sender.receive_data
