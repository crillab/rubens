# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.1] - 2020-03-08
### Changed
- migrated unit tests to junit5
- cleaned code

### Fixed
- updated maven plugins to fix incompatibilities with recent JDKs

## [1.1.0] - 2021-02-23
### Added
- added a checker for ICCMA'21 CE track

## [1.0.2] - 2021-02-23
### Fixed
- bumped junit version to 4.13.1 to fix a vulnerability of version 4.12

## [1.0.1] - 2019-07-03
### Fixed
- unit tests could make the compilation process crash on non-UNIX systems
- fixed an issue with maven license plugin

## [1.0.0] - 2019-06-20
### Added
- added core implementation of RUBENS
- added generators for SAT solvers and model counters
- added generators for ICCMA'17 and ICCMA'19 tracks
- added checkers for SAT and model counting
- added checkers for ICCMA'17 and ICCMA'19 tracks
