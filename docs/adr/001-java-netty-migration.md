# ADR-001: Use Java/Netty for the reconstructed gateway

## Context

A socket gateway must handle framing, connection lifecycle, validation, diagnostics, and downstream handoff without mixing every concern in one monolithic loop.

## Decision

Use Java with Netty and split the pipeline into independent handlers.

## Why

- Explicit connection/pipeline lifecycle
- Reusable framing and validation handlers
- Easier automated testing than native socket code
- Familiar operational model for Java-centric teams
- Clear extension point for TLS and partner-specific configuration

## Trade-offs

- Netty requires care around reference-counted buffers and event-loop blocking.
- Business work must not block I/O threads.
- A framework does not eliminate the need for protocol-level compatibility testing.
