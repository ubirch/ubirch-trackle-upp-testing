## run local server

```
mvn compile
mvn exec:java -Dexec.mainClass=com.ubirch.ActorServer
```

## get new MsgPack

with hex string
```
curl -X POST \
  http://localhost:8080/newMsgPackString \
  --data "${msgpack}"
```

with binary
```
echo "${msgPack}" | xxd -r -ps > msgPack.bin
curl -X POST \
  http://localhost:8080/newMsgPackByBin \
  --data-binary @msgPack.bin
```
