import 'dart:collection';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Method Channel Test',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const MethodChannel _channel =
  const MethodChannel('com.example.flutterNativeTest');

  String _platformVersion = 'Unknown';
  String _headphoneState = "45";
  String _userState = "";
  int _additionResult = 0;
  int a,b;

  Future<String> getPlatformVersion() async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  Future<int> addition(int a, int b) async {
    return await _channel.invokeMethod("addition", <String, dynamic>{
      "a": a,
      "b": b,
    });
  }

  Future<String> getUserState() async {
    LinkedHashMap<dynamic, dynamic> result = await _channel.invokeMethod("getUserState");
    print(result);
    return "정확도 : ${result['confidence']}, 활동 : ${result['activity']}";
  }

  Future<String> getHeadphoneState() async {
    final String result = await _channel.invokeMethod("getHeadphoneState");
    return result;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Method Channel Test"),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            RaisedButton(
              child: Text("Get Platform Version"),
              onPressed: () async {
                String result = await getPlatformVersion();
                setState(() {
                  _platformVersion = result;
                });
              },
            ),
            RaisedButton(
              child: Text("헤드폰 상태 가져오기"),
              onPressed: () async {
                String result = await getHeadphoneState();
                setState(() {
                  _headphoneState = result;
                });
              },
            ),
            RaisedButton(
              child: Text('사용자 상태 가져오기'),
              onPressed: () async {
                String result = await getUserState();
                setState(() {
                  _userState = result;
                });
              },
            ),
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text("a = "),
                Container(
                  width: 50,
                  child: TextField(
                    onChanged: (value) {
                      setState(() {
                        a = int.parse(value);
                      });
                    },
                    textAlign: TextAlign.center,
                  ),
                ),
                Text("b = "),
                Container(
                  width: 50,
                  child: TextField(
                    onChanged: (value) {
                      setState(() {
                        b = int.parse(value);
                      });
                    },
                    textAlign: TextAlign.center,
                  ),
                )
              ],
            ),
            RaisedButton(
              child: Text("더하기"),
              onPressed: () async {
                int result = await addition(a, b);
                setState(() {
                  _additionResult = result;
                });
              },
            ),
            Text(_platformVersion),
            Text("더하기 결과 $_additionResult"),
            Text("헤드폰 상태 $_headphoneState"),
            Text('사용자 상태 $_userState')
          ],
        ),
      ),
    );
  }
}