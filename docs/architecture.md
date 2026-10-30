# Architecture

## Goal

Separate transport, framing, validation, business handling, error history, and downstream registration so each concern can be tested and changed independently.

```mermaid
flowchart TD
    C[External Partner - synthetic] --> E[Edge / Reverse Proxy concept]
    E --> S[Netty Server]
    S --> D[Length Frame Decoder]
    D --> J[UTF-8 JSON Decode]
    J --> V[Validation Handler]
    V -->|valid| B[Business Handler]
    B --> Q[Async Registration Service]
    V -->|invalid| H[Error Record Repository]
    H --> R[Manual Reprocess Service]
    R --> V
```

## Boundaries

- **Transport**: TCP connection and frame boundaries only.
- **Protocol**: parse fictional JSON payload.
- **Validation**: required-field, type, size, checksum checks.
- **Business**: accept valid document-registration request.
- **Async registration**: simulate internal downstream processing.
- **Error history**: retain synthetic raw payload and reason for diagnosis/reprocessing.

No production network topology or internal naming is reproduced.
