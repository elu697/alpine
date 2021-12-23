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


class Producer():
    def __init__(self):
        self.app = NDNApp()
        self.prefix
        self._current_prefixes = []
        self.app.run_forever(after_start=self.main())

    def main(self):
        self.set_route("/ex1")

    def _on_interest(name: FormalName, param: InterestParam, _app_param: Optional[BinaryStr]):
        print("on_interest")
        interest_json = json.loads(str(_app_param), 'utf8')
        pprint.pprint(interest_json, width=40)

        content = "HELLO"
        json_dict = {"data": content}
        response_json = json.dumps(json_dict)
        content = response_json.encode()
        self.app.put_data(name, content=content, freshness_period=10000)

    def _on_msg_interest(self, int_name, int_param, app_param):
        aio.ensure_future(self._on_interest(
            int_name, int_param, app_param))

    def test(**kwargs):
        print(kwargs)

    async def set_route(self, prefix):
        print("set_route")
        if Name.is_prefix(prefix, name_at_repo):
            self.app.set_interest_filter(prefix, func=self._on_interest)
        self._current_prefixes.append(prefix)
        print("wait")
        # return await self.app.register(prefix, func=self.test)

    async def unset_route(self, prefix):
        try:
            i = self._current_prefixes.index(prefix)
            self._current_prefixes.pop(i)
            return self.app.unset_interest_filter(prefix)
        except e:
            print(e)
            return False

    @ staticmethod
    async def repop_interest():
        pass


if __name__ == "__main__":
    p = Producer()
    p.set_route("/ex")
