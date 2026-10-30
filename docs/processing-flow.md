# Processing Flow

## Success

```mermaid
sequenceDiagram
    participant P as Partner
    participant G as Netty Gateway
    participant V as Validator
    participant A as Async Registration

    P->>G: length-prefixed dummy JSON
    G->>V: parsed frame
    V-->>G: valid DummyMessage
    G->>A: enqueue registration
    G-->>P: OK | requestId
```

## Validation failure

```mermaid
sequenceDiagram
    participant P as Partner
    participant G as Netty Gateway
    participant V as Validator
    participant E as Error History

    P->>G: malformed/invalid dummy message
    G->>V: validate
    V->>E: save raw synthetic message + reason
    G-->>P: FAIL | errorId
```

## Reprocessing

The sample keeps the original synthetic payload for diagnosis. An operator can later select one error record and retry it through `ReprocessService`. The same `MessageValidator` used during initial receipt is reused before downstream registration.

The production implementation that inspired this portfolio used a richer operational flow, but none of its real schemas, fields, or message layouts are reproduced here.
