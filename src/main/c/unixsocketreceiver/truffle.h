#ifndef TRUFFLEHOG_TRUFFLE_H
#define TRUFFLEHOG_TRUFFLE_H

/**
 * @file
 * @brief The structure of a Truffle that is sent via ipc.
 */

#include <stdint.h>

/**
 * @brief Houses specific information about the ether header
 *
 */
struct EtherHeader
{
	uint64_t sourceMacAddress;
	uint64_t destMacAddress;
	uint16_t etherType;
};

/**
 * @brief Houses specific information about the profinet frame
 */
struct Frame
{
	uint16_t frameID;
	char destName[30];
	char srcName[30];
	long long cycleCounter;
};

typedef struct Truffle
{
	uint64_t flags;

	struct EtherHeader etherHeader;

	struct Frame frame;
} Truffle_t;

#endif
