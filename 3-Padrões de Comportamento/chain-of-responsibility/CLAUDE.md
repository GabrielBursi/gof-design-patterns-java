# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Purpose

This is a study project for the **Chain of Responsibility** GoF behavioral design pattern, developed as part of the branas.io Design Patterns course. The goal is to implement the pattern following DDD, Clean Architecture, TDD, and SOLID principles in Java 24.

Inspired by: https://github.com/rodrigobranas/design_patterns/tree/master/src/gof/behavioral/chain_of_responsibility

## Commands

```bash
# Build and run all tests
mvn test

# Run a single test class
mvn test -Dtest=YourTestClassName

# Run a single test method
mvn test -Dtest=YourTestClassName#methodName

# Build without running tests
mvn compile
```

## Architecture

The project uses **Java 24** with **Maven** and no frameworks (pure Java). Dependencies are limited to JUnit 5 and Mockito for testing.

### Chain of Responsibility Structure

The pattern requires these participants:
- **Handler interface** — declares `setNext(Handler): Handler` and `handle(Request): Response`
- **BaseHandler (abstract)** — implements default forwarding to successor via `super.handle()`
- **ConcreteHandlers** — extend BaseHandler, process or delegate; `setNext` returns the handler to allow fluent chaining: `h1.setNext(h2).setNext(h3)`
- **Request object** — encapsulates request data passed through the chain

### Development Approach

- **TDD**: write failing tests first, implement to make them pass, then refactor
- **DDD**: model the domain with rich objects, not anemic data holders
- **SOLID**: handlers follow SRP (one responsibility per handler) and OCP (new handlers added without changing existing ones)

## Available Skills

Custom skills are installed in `.agents/skills/` and provide reference material for:
- `gof-patterns/gof-behavioral/chain-of-responsibility.md` — full pattern reference with pseudocode and examples
- `tdd/` — TDD red-green-refactor guidance and mocking patterns
- `ddd-tactical-patterns/` — DDD tactical patterns (entities, value objects, aggregates)
- `tactical-ddd/` — domain model validation and refactoring
- `java-coding-standards/` — naming, immutability, Optional, streams conventions
- `solid-principles/` — SOLID principles application guide
- `clean-ddd-hexagonal/` — Clean Architecture layering reference

Use `/tdd`, `/ddd-tactical-patterns`, `/solid-principles`, etc. to invoke these skills when implementing features.
