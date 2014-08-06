# node-js-seq-diagrams

To generate svg sequence diagrams, pipe a definition into `node index.js`.

```
echo "A->B: Hello\nB->C: Hi" | node index.js > test.svg
```

Read about the format here: http://bramp.github.io/js-sequence-diagrams/
