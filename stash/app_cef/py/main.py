import cefpyco as cp
import asyncio


class CP_MODE:
    CONSUMER = 0
    PUBLISHER = 1


class CP_Session:
    def __init__(self, name, mode=CP_MODE.CONSUMER):
        self.handle = cp.CefpycoHandle()
        self.name = name
        self.mode = mode

        self.connect()

    def connect(self):
        try:
            self.handle.begin()
        except Exception as e:
            AssertionError(e)

    def disconnect(self):
        try:
            self.handle.end()
        except Exception as e:
            AssertionError(e)

    def start(self, closure):
        if self.mode == CP_MODE.CONSUMER:
            self.handle.send_interest(name=self.name, lifetime=4000)

        elif self.mode == CP_MODE.PUBLISHER:
            self.handle.register(self.name)

    def __deinit__(self):
        self.disconnect()


class CP_Manager:
    def __init__(self):
        self.session = None

    def interest(self, uri, receive_closure):
        self.session = CP_Session(name=uri, mode=CP_MODE.CONSUMER)
        self.session.start(receive_closure)
        res = self.session.receive()
        res. in

    def register(self, uri, response_closure):
        self.session = CP_Session(name=uri, mode=CP_MODE.PUBLISHER)
        self.session.start(response_closure)


def test():
    print("A")


if __name__ == '__main__':
    manager = CP_Manager()
    manager.interest("ccnx:/test", test())
