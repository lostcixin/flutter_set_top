import 'dart:async';

import 'package:flutter/services.dart';

class Settop {
  static const MethodChannel _channel =
      const MethodChannel('settop');

  static void setTopApp()  {
   _channel.invokeMethod('setTopApp');
  }
}
