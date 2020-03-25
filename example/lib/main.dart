import 'package:flutter/material.dart';
import 'dart:async';

import 'package:settop/settop.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    Timer timer = new Timer(new Duration(seconds: 30), () {
      Settop.setTopApp();
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: FlatButton(onPressed: (){initPlatformState();}, child: Text('start')),
        ),
      ),
    );
  }
}
