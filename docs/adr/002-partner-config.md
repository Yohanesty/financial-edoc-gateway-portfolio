# ADR-002: Isolate partner-specific differences as configuration

## Context

External partners often share one overall processing flow but differ in operational constraints and validation options.

## Decision

Represent partner differences through `PartnerProfile` rather than duplicating handlers.

## Benefit

The common transport/validation/business path remains stable while partner-specific settings can evolve independently.

## Confidentiality note

The settings in this repository are fictional and intentionally do not reflect any real institution or VAN provider.
