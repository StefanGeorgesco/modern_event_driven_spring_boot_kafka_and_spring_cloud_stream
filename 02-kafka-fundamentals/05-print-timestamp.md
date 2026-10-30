## Print Timestamp

```bash
./kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic demo-topic \
    --formatter-property print.offset=true \
    --formatter-property print.timestamp=true \
    --from-beginning
```
