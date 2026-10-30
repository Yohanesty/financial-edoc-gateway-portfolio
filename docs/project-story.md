# Project Story — Sanitized Portfolio Version

## Problem

The production-inspired project involved a legacy external message-receiving service with several maintainability and operational challenges:

- Native socket code was difficult to maintain and transfer between engineers.
- Transport, parsing, validation, and business processing were tightly coupled.
- Diagnostic information was insufficient when malformed or incomplete messages arrived.
- Security and runtime modernization had to be considered without breaking external integration behavior.

## Engineering decisions

### 1. Move the receiving layer to Java/Netty

Netty was selected to make connection lifecycle, framing, handler composition, and non-blocking I/O explicit while keeping the implementation in the team's primary Java ecosystem.

### 2. Separate transport from validation and business handling

The reconstructed sample divides the flow into frame decoding, JSON decoding, message validation, business handling, and asynchronous downstream registration. This is intentionally simpler than the production system but demonstrates the same separation-of-concerns principle.

### 3. Keep partner differences outside common business logic

`PartnerProfile` represents partner-specific options in the sample. The goal is to avoid duplicating the transport and processing pipeline for every external counterpart.

### 4. Retain failed messages for controlled recovery

Invalid synthetic messages are stored with a normalized failure reason. Reprocessing is explicit and reuses the same validator as initial receipt, rather than bypassing validation or retrying indefinitely.

## Reconstructed solution

```text
External Partner
      |
      v
Length-Prefixed TCP Frame
      |
      v
Netty Frame Decoder
      |
      v
Protocol Decode
      |
      v
Shared Message Validator -----> Error History
      |                              |
      v                              v
Business Handler <----------- Manual Reprocess
      |
      v
Async Downstream Registration
```

## What this repository intentionally does not reproduce

- Real financial message layouts or transaction fields
- Real partner-specific retry rules or network configuration
- Production database tables, SQL, or encryption implementation
- Internal monitoring, deployment, security, or incident artifacts
- Employer or client source code

## Interview focus

This repository is intended to support discussion of architectural decisions rather than to claim that a public sample is identical to a production system. Useful discussion topics include framing, event-loop blocking, error acknowledgement, controlled reprocessing, partner configuration, logging boundaries, and migration compatibility testing.
