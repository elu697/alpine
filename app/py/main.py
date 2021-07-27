import cefpyco as cp
import asyncio


class CP_mode:
    CONSUMER = 0
    PUBLISHER = 1


class CP_Session:
    def __init__(self, name, mode=CP_mode.CONSUMER):
        self.handle = cefpyco.CefpycoHandle()
        self.name = name
        self.mode = mode

        self.connect()
        self.start()

    def connect(self):
        try:
            self.handle.begin()
        except e:
            AssertionError(e)

    def disconnect(self):
        try:
            self.handle.end()
        except e:
            AssertionError(e)

    def start(self, closure):
        if self.mode == CP_MODE.CONSUMER:
            self.handle.send_interest(self.name)

        elif self.mode == CP_MODE.PUBLISHER:
            self.handle.register(self.name)

    def __deinit__(self):
        self.disconnect()


class CP_Manager:
    def __init__(self):
        self.session = None

    def interest(self, uri, receive_closure):
        self.session = CP_Session(name=uri, mode=CP_mode.CONSUMER)

    def register(self, uri, response_closure):
        self.session = CP_Session(name=uri, mode=CP_mode.PUBLISHER)
