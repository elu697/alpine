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
                    level=logging.DEBUG,
                    style='{')


class Consumer():
    def __init__(self):
        self.app = NDNApp()
        self._name = None
        self._param = None
        self._callback = None

    def send_interest(self, uri, data=None, callback=None):
        print("send_interest")
        self._name = Name.from_str(uri)
        json_dict = {"data": data}
        interest_json = json.dumps(json_dict)
        pprint.pprint(interest_json, width=40)
        self._param = interest_json.encode()
        self._callback = callback
        self.app.run_forever(after_start=self.process())

    async def process(self):
        try:
            print("process")
            name = self._name
            param = self._param
            data_name, meta_info, content = await self.app.express_interest(
                name, app_param=param, must_be_fresh=True, can_be_prefix=False, lifetime=6000, hop_limit=2)

            json_data = json.loads(str(content, 'utf8'))
            self._callback(data_name, json_data)
        except InterestNack as e:
            print(f'Nacked with reason={e.reason}')
        except InterestTimeout:
            print(f'Timeout')
        except InterestCanceled:
            print(f'Canceled')
        except ValidationFailure:
            print(f'Data failed to validate')
        finally:
            self.app.shutdown()


if __name__ == '__main__':
    c = Consumer()
    c.send_interest("/ex")
