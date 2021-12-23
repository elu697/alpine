import lcn_fowarder as lcn


def callback(data_name, json_data):
    print("callback")
    print(data_name)


c = lcn.Consumer()
c.send_interest("/ex1", data="hello", callback=callback)
