#!/usr/bin/python
#
# pybuddyDX
# python e-buddy (ibuddy alike sold on DX) daemon
# http://code.google.com/p/pybuddyDX
#
# protocol reverse engineered and implemented by
# peter.dhoye@gmail.com
#
# borrows code from http://code.google.com/p/pybuddy
# by Jose.Carlos.Luna@gmail.com and luis.peralta@gmail.com
# who got most of the code from http://cuntography.com/blog/?p=17
# Which is based on http://scott.weston.id.au/software/pymissile/

import usb
import time
import sys
import socket
import os
import pwd
import logging
from ConfigParser import RawConfigParser

################
#Commands
################
# GLADNESS =        00
# FEAR =            01
# FIZZ =            02
# PLEASANTSURPRISE =03
# GRIEF = 			04
# FURY = 			05
# QUELL = 			06
# REDHEAD = 		07
# GREENHEAD = 		08
# BLUEHEAD = 		09
# YELLOWHEAD =		10
# BLAME = 			11
# BLUEGREENHEAD =	12
# WHITEHEAD = 		13
# HEART = 			14
# WINGS = 			15
# BODY = 			16
# NOEFFECT = 		17
# ONLINE = 			18
# BUSY = 			19
# DAZE = 			20
# BACKSOON = 		21
# AWAY = 			22
# PHONE = 			23
# LUNCH = 			24
# OFFLINE = 		25

################
#Configuration
################
tsleep = 0.1


################
# IBUDDY class
################

class BuddyDevice:
  SETUP   = (0x21, 0x09, 0x00, 0x02, 0x00, 0x00, 0x04, 0x00)
  MESS    = (0x43, 0x4D)
  
  OFF1 = 0x31
  OFF2 = 0x37

  code1 = OFF1
  code2 = OFF2

  def __init__(self):
    try:
      self.dev=UsbDevice(0x0c45, 0x11)
      self.dev.open()
      self.dev.handle.reset()
      self.resetMessage()
    except NoBuddyException, e:
      raise NoBuddyException()
      
  def resetMessage(self):
     self.code1 = self.OFF1
     self.code1 = self.OFF1
     self.send()

  def send(self):
    try:
        self.dev.handle.controlMsg(0x21, 0x09, self.SETUP, 0x0200, 0x00)
        self.dev.handle.controlMsg(0x21, 0x09, self.MESS+(self.code1,self.code2), 0x0200, 0x00)
    except usb.USBError:
		log.info("Error sending USB command")
		raise NoBuddyException()

#####################
# USB class
######################

class UsbDevice:
  def __init__(self, vendor_id, product_id):
    busses = usb.busses()
    self.handle = None
    for bus in busses:
      devices = bus.devices
      for dev in devices:
        if dev.idVendor==vendor_id and dev.idProduct==product_id:
          log.info("DX e-buddy found!")
#          log.info("vend %s  prod %s",dev.idVendor, dev.idProduct)
          self.dev = dev
          self.conf = self.dev.configurations[0]
          self.intf = self.conf.interfaces[0][0]
          self.endpoints = []
#          log.info("interface = %x, class = %s, protocol = %s", self.intf.interfaceNumber, self.intf.interfaceClass, self.intf.interfaceProtocol)
          for endpoint in self.intf.endpoints:
            self.endpoints.append(endpoint)
#            log.info("endpoint number = %x, type = %s", endpoint.address, endpoint.type)
          return
    raise NoBuddyException()

  def open(self):
    if self.handle:
      self.handle = None
    self.handle = self.dev.open()

#    if self.handle:
#	   log.info("Handle OK")

    #We need to detach HID interface
    try:
        self.handle.detachKernelDriver(0)
        self.handle.detachKernelDriver(1)
    except:
        pass

    try:
        self.handle.setConfiguration(self.conf)
        self.handle.claimInterface(0)
        self.handle.setAltInterface(0)
    except:
        log.info("Configuration failed")
        raise NoBuddyException()

#    log.info("Device opened OK")

class NoBuddyException(Exception): pass


#########################################
# Decoding macros
##########################################


def decode_buddy (buddy,msg):
#    log.info("Received message: %s",msg)
    buddy.code1 = int(msg)/10 + 0x30
    buddy.code2 = int(msg) - (int(msg)/10)*10 + 0x30
#    log.info("Codes: %x %x",buddy.code1,buddy.code2)

#######################################
# MAIN program
#######################################

log = logging.getLogger('pybuddy')

#Default config
config = RawConfigParser(
            { 'port': 8888,
              'address': '127.0.0.1',
              'user': 'nobody',
              'loglevel': 'info',
              'logfile': 'console',
             }
)

config._sections = {'network':{}, 'system':{}}

config_files = [ "~/.pybuddy.cfg", 
                 "/etc/pybuddy/pybuddy.cfg", 
                 "/usr/local/etc/pybuddy.cfg"
]

#Parse config
if len(sys.argv) > 1:
    config_files.append(sys.argv[1])
    
config_read = config.read(config_files)

if config.get("system", "logfile") != "console":
    logging.basicConfig(
                        filename=config.get("system", "logfile"),
                        format='%(asctime)s %(levelname)-8s %(message)s',
    )
else:
    logging.basicConfig(
                        stream=sys.stderr,
                        format='%(asctime)s %(levelname)-8s %(message)s',
    )


if config.get("system", "loglevel") == "debug":
    log.setLevel(logging.DEBUG)
elif config.get("system", "loglevel") == "info":
    log.setLevel(logging.INFO)


if config_read:
    log.info("Read config file: %s", config_read[0])
    
#Initialize device
log.info("Searching e-buddy...")
try:
    buddy=BuddyDevice()
except NoBuddyException, e:
    log.error("Not found or ERROR!")
    sys.exit(1)


#Daemonize
log.info("Starting daemon...")
if os.fork()==0:
    os.setsid()
else:
    sys.exit(0)

#Create server socket
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
s.bind((config.get("network", "address"), int(config.get("network", "port"))))

#Drop privileges
try:
    uid = pwd.getpwnam(config.get("system", "user"))[2]
except KeyError:
    log.error("Username %s not found, exiting...", config.get("system", "user"))
    sys.exit(1)
os.setuid(uid)


#Main message loop
while 1:
    try:
        message, address = s.recvfrom(8192)
#        log.debug("Got data from %s", address)
        decode_buddy(buddy, message)
        buddy.send()
    except (KeyboardInterrupt, SystemExit):
        raise

      
