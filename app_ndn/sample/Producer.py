from ndn.app import NDNApp
from ndn.encoding import Name, Component, InterestParam, BinaryStr, FormalName, MetaInfo
from ndn.types import InterestNack, InterestTimeout, InterestCanceled, ValidationFailure
from ndn.schema import policy
from ndn.schema.schema_tree import Node, MatchedNode
from ndn.schema.simple_node import RDRNode
from ndn.schema.simple_cache import MemoryCache, MemoryCachePolicy
from ndn.schema.simple_trust import SignedBy
from typing import Optional
from collections import OrderedDict
import ndn.utils
import asyncio as aio
import logging
import sys
import json
import pprint
import nest_asyncio
nest_asyncio.apply()


logging.basicConfig(format='[{asctime}]{levelname}:{message}',
                    datefmt='%Y-%m-%d %H:%M:%S',
                    level=logging.INFO,
                    style='{')


app = NDNApp()


async def repop(NApp):
    OPTION_DATA = s = r'{"C": "\u3042", "A": {"i": 1, "j": 2}, "B": [{"X": 1, "Y": 10}, {"X": 2, "Y": 20}], "REPOP": 0}'
    try:
        timestamp = ndn.utils.timestamp()
        name = Name.from_str('/example/testApp/randomData') + \
            [Component.from_timestamp(timestamp)]
        print(
            f'Sending Interest {Name.to_str(name)}, {InterestParam(must_be_fresh=True, lifetime=6000)}')
        data_name, meta_info, content = await NApp.express_interest(
            name, app_param=OPTION_DATA.encode(), must_be_fresh=True, can_be_prefix=False, lifetime=6000)

        print(f'Received Data Name: {Name.to_str(data_name)}')
        print(meta_info)
        print(bytes(content) if content else None)
    except InterestNack as e:
        print(f'Nacked with reason={e.reason}')
    except InterestTimeout:
        print(f'Timeout')
    except InterestCanceled:
        print(f'Canceled')
    except ValidationFailure:
        print(f'Data failed to validate')
    finally:
        NApp.shutdown()


@app.route('/example/testApp')
def on_interest(name: FormalName, param: InterestParam, _app_param: Optional[BinaryStr]):
    print("len ->", len(_app_param))
    OPTION_DATA = str(_app_param, 'utf8')
    db = json.loads(OPTION_DATA)
    pprint.pprint(db, width=40)

    print("App param", OPTION_DATA)
    print(f'>> I: {Name.to_str(name)}, {param}, {_app_param}')
    content = "Hello, world!".encode()
    app.put_data(name, content=content, freshness_period=10000)
    print(f'<< D: {Name.to_str(name)}')
    print(MetaInfo(freshness_period=10000))
    print(f'Content: (size: {len(content)})')
    print('')

    if db["REPOP"] == 1:
        NApp = NDNApp()
        NApp.run_forever(after_start=repop(NApp))


if __name__ == '__main__':
    app.run_forever()
