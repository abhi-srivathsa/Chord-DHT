# Chord DHT

A Java implementation of the **Chord** distributed hash table (DHT) protocol, as described in the landmark 2001 paper by Stoica et al. This project implements a peer-to-peer key lookup system over a consistent hash ring using SHA-1 based finger tables.

## What is Chord?

Chord is a protocol and algorithm for a distributed hash table. It efficiently distributes data across a network of nodes and provides fast key-based lookups even as nodes join and leave. Each node maintains a small routing table (finger table) that enables O(log N) lookups across N nodes.

## Features

- **Consistent Hashing** — Nodes and keys are mapped onto a circular identifier space using SHA-1
- **Finger Table Routing** — O(log N) key lookup via finger tables
- **Node Join/Leave** — Dynamic node joining with successor/predecessor pointer maintenance
- **Stabilization** — Periodic stabilization to handle node churn
- **Java + Maven** — Built with standard Java and managed with Maven

## Tech Stack

- **Language:** Java
- **Build Tool:** Maven
- **Protocol:** Chord DHT (Stoica et al., 2001)

## Project Structure

```
Chord-DHT/
├── src/
│   └── main/java/
│       └── chord/        # Core Chord protocol implementation
├── pom.xml               # Maven build config
└── chord.iml             # IntelliJ project file
```

## Getting Started

### Prerequisites
- Java 8+
- Maven

### Build & Run

```bash
git clone https://github.com/abhi-srivathsa/Chord-DHT.git
cd Chord-DHT
mvn compile
mvn exec:java
```

## Key Concepts Implemented

- **SHA-1 consistent hashing** for node and key ID assignment
- **Finger table** construction and routing
- **Successor/predecessor** pointer management
- **Stabilization and fix-fingers** periodic routines
- **Node join protocol** for integrating new peers

## Background

Built as part of coursework in **Distributed Systems** at Santa Clara University (M.S. Computer Science, 2021–2022). Based on the original Chord paper: *"Chord: A Scalable Peer-to-peer Lookup Service for Internet Applications"* — Stoica, Morris, Karger, Kaashoek, Balakrishnan (SIGCOMM 2001).
