#!/bin/bash

sudo csmgrdstart
sleep 1
sudo cefnetdstart

csmgrstatus
cefstatus
