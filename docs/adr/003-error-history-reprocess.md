# ADR-003: Preserve failed synthetic messages for diagnosis and reprocessing

## Context

A failure response alone is not sufficient for operational analysis. The system needs an auditable way to understand what failed and retry after the underlying issue is resolved.

## Decision

Store the synthetic raw message and a normalized failure reason in an error-history repository. Reprocessing is an explicit operation rather than an automatic infinite retry, and the same validator is reused before downstream registration.

## Benefit

- Better troubleshooting evidence
- Controlled manual recovery
- Consistent validation between initial receipt and reprocessing
- Separation of receive-time acknowledgement from later recovery work

## Portfolio scope

The repository uses an in-memory store only. No real database table, column, or production reprocessing rule is reproduced.
