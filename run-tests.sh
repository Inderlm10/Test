#!/bin/bash

# Web Testing Framework - Test Runner Script
# This script provides easy commands to run tests with different configurations

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "Web Testing Framework - Test Runner"
    echo ""
    echo "Usage: $0 [COMMAND] [OPTIONS]"
    echo ""
    echo "Commands:"
    echo "  build           Build the project"
    echo "  test            Run all tests"
    echo "  test-chrome     Run tests with Chrome browser"
    echo "  test-firefox    Run tests with Firefox browser"
    echo "  test-headless   Run tests in headless mode"
    echo "  test-parallel   Run tests in parallel (4 threads)"
    echo "  test-specific   Run specific test class"
    echo "  clean           Clean project and reports"
    echo "  help            Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build"
    echo "  $0 test"
    echo "  $0 test-chrome"
    echo "  $0 test-specific GoogleSearchTest"
    echo "  $0 test-parallel"
}

# Function to check prerequisites
check_prerequisites() {
    print_status "Checking prerequisites..."
    
    # Check Java
    if ! command -v java &> /dev/null; then
        print_error "Java is not installed or not in PATH"
        exit 1
    fi
    
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$java_version" -lt 11 ]; then
        print_error "Java 11 or higher is required. Current version: $java_version"
        exit 1
    fi
    
    print_success "Java version: $(java -version 2>&1 | head -n 1)"
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        print_error "Maven is not installed or not in PATH"
        exit 1
    fi
    
    print_success "Maven version: $(mvn -version | head -n 1)"
}

# Function to build project
build_project() {
    print_status "Building project..."
    mvn clean compile
    print_success "Project built successfully"
}

# Function to run tests
run_tests() {
    local browser=${1:-chrome}
    local headless=${2:-false}
    local parallel=${3:-1}
    
    print_status "Running tests with browser: $browser, headless: $headless, parallel: $parallel"
    
    mvn test \
        -Dtest.browser="$browser" \
        -Dtest.headless="$headless" \
        -Dtest.parallelThreads="$parallel"
    
    print_success "Tests completed"
}

# Function to run specific test
run_specific_test() {
    local test_class=$1
    if [ -z "$test_class" ]; then
        print_error "Test class name is required"
        echo "Usage: $0 test-specific <TestClassName>"
        exit 1
    fi
    
    print_status "Running specific test: $test_class"
    mvn test -Dtest="$test_class"
    print_success "Test completed"
}

# Function to clean project
clean_project() {
    print_status "Cleaning project and reports..."
    mvn clean
    rm -rf screenshots/
    rm -rf logs/
    rm -rf test-output/
    print_success "Project cleaned successfully"
}

# Main script logic
case "$1" in
    "build")
        check_prerequisites
        build_project
        ;;
    "test")
        check_prerequisites
        build_project
        run_tests
        ;;
    "test-chrome")
        check_prerequisites
        build_project
        run_tests "chrome" "false" "1"
        ;;
    "test-firefox")
        check_prerequisites
        build_project
        run_tests "firefox" "false" "1"
        ;;
    "test-headless")
        check_prerequisites
        build_project
        run_tests "chrome" "true" "1"
        ;;
    "test-parallel")
        check_prerequisites
        build_project
        run_tests "chrome" "false" "4"
        ;;
    "test-specific")
        check_prerequisites
        build_project
        run_specific_test "$2"
        ;;
    "clean")
        clean_project
        ;;
    "help"|"--help"|"-h"|"")
        show_usage
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_usage
        exit 1
        ;;
esac 