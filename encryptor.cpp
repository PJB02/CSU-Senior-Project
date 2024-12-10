#include <iostream>
#include <fstream>
#include <vector>
#include <string>

void encryptDecrypt(std::string& data, char key)
{
    for (size_t i = 0; i < data.size(); ++i)
	{
        data[i] ^= key; // XOR operation
    }
}

void encryptFile(const std::string& inputFileName, const std::string& outputFileName, char key)
{
    std::ifstream inputFile(inputFileName, std::ios::binary);
    if (!inputFile)
	{
        std::cerr << "Error opening input file: " << inputFileName << std::endl;
        return;
    }

    std::string data((std::istreambuf_iterator<char>(inputFile)), std::istreambuf_iterator<char>());
    inputFile.close();

    encryptDecrypt(data, key); // Encrypt or decrypt the data

    std::ofstream outputFile(outputFileName, std::ios::binary);
    if (!outputFile)
    {
        std::cerr << "Error opening output file: " << outputFileName << std::endl;
        return;
    }

    outputFile.write(data.c_str(), static_cast<std::streamsize>(data.size())); // Explicit cast
    outputFile.close();

}

int main()
{
    const std::string inputFile = "testing.txt";
    const std::string outputFile = "encrypted.txt";
    char key = 'K'; // Simple key for XOR

    encryptFile(inputFile, outputFile, key);

    std::cout << "File encrypted successfully and saved as " << outputFile << std::endl;
    return 0;
}
