cmake_minimum_required(VERSION 3.10.2)
project(PhonemizerApp)

# Cria a biblioteca nativa do seu projeto
add_library(phonemizer SHARED
        src/main/jni/phonemizer_jni.cpp)

# Caminho para bibliotecas .so pré-compiladas (jniLibs/*/libttsespeak.so)
set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs)

# Adiciona os diretórios corretos de includes (se futuramente tiver headers)
include_directories(${distribution_DIR}/include)

# Adiciona o caminho das libs por ABI (armeabi-v7a, arm64-v8a, etc.)
link_directories(${distribution_DIR}/${ANDROID_ABI})

# Indica que ttsespeak será resolvido dinamicamente no link
add_library(ttsespeak SHARED IMPORTED)
set_target_properties(ttsespeak PROPERTIES
        IMPORTED_LOCATION ${distribution_DIR}/${ANDROID_ABI}/libttsespeak.so)

# Linka phonemizer com ttsespeak e log
target_link_libraries(phonemizer
        ttsespeak
        log)
