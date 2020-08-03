#include<iostream>

void memory_copy(int * destination, int * source, unsigned int size);
void iteration_copy(int * destination, int * source, unsigned int size);

int main() {
    int[] source = new int[0x01000000];
    int[] destination = new int[0x01000000];


}

void memory_copy(int * destination, int * source, unsigned int size) {
    std::memcpy(destination, source, size);
}

void iteration_copy(int * destination, int * source, unsigned int size) {
    for(unsigned int i = 0; i < size; i++) {
        source[i] = destination[i];
    }
}