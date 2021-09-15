from ndn.app import *
from ndn.encoding import *
from ndn.types import *
import ndn.utils
import asyncio as aio
from typing import Optional
import logging


logging.basicConfig(format='[{asctime}]{levelname}:{message}',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    level=logging.INFO,
                    style='{')


app = NDNApp()


@app.route('/example/testApp')
def on_interest(name: FormalName, param: InterestParam, _app_param: Optional[BinaryStr]):
    # print(bytes(_app_param) if _app_param else None)
    # print(str(_app_param, 'utf8'))
    print(f'>> I: {Name.to_str(name)}, {param}, {_app_param}')
    content = "Hello, world!".encode()
    app.put_data(name, content=content, freshness_period=10000)
    print(f'<< D: {Name.to_str(name)}')
    print(MetaInfo(freshness_period=10000))
    print(f'Content: (size: {len(content)})')
    print('')


if __name__ == '__main__':
    app.run_forever()
