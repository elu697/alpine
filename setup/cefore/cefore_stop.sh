#!/bin/bash

sudo cefnetdstop
sleep 1
sudo csmgrdstop

csmgrstatus
cefstatus
