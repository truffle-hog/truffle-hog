#ifndef __TRUFFLE_H__
#define __TRUFFLE_H__

#define MAX_STRING_LEN 32

#define MAX_BLOCKS 32

/**
 * @file
 * @brief The structure of a Truffle that is send via ipc.
 */

// TODO define the flags that are used within the truffle

#include <stdint.h>

/**
 * @brief Houses specific information about the ether header.
 *
 */
struct EtherHeader {
  uint64_t sourceMacAddress;
  uint64_t destMacAddress;
  uint16_t etherType;
};

struct DeviceBlock {

	char nameOfStation[MAX_STRING_LEN];
};


struct Block {

	enum {
		IS_DEVICE
	} type;

	union {

		struct DeviceBlock deviceBlock;

	} val;


};

struct RTC1 {};



struct DCP {

	uint8_t serviceID;
	char serviceIDName[MAX_STRING_LEN];
	uint8_t serviceType;
	char serviceTypeName[MAX_STRING_LEN];
    /** 0 no response, 1 response **/
    uint8_t isResponse;

	uint32_t xID;
	uint16_t responseDelay;

	uint16_t dataLength;

	struct Block blocks[MAX_BLOCKS];
};

/**
 * @brief Houses specific information about the frame.
 */
struct Frame {
  uint16_t id;

  enum {
  	IS_DCP, IS_RTC1
  } type;

  union {
	 struct RTC1 rtc1;
	 struct DCP dcp;

  } val;

  char destName[MAX_STRING_LEN];
  char srcName[MAX_STRING_LEN];
  long long cycleCounter;
};

/**
 * @brief The datastructure for sending relevant information to another process.
 *
 * The Truffle is the datastructure that encapsulates all necessary and important
 * information about a processed Network Packet. The structure of the Truffle is also
 * known by the clients that want to receive information about the network package.
 *
 * Like this clients are able to cast incoming data to this data type and imediately
 * read out the relevant data.
 *
 */
struct Truffle {

  /** @brief Flags are used for specific boolean states that are relevant for the whole package. **/
  uint64_t flags;
  /** @brief The Etherheader holds information from the etherheader of the network package. **/
  struct EtherHeader etherHeader;
  /** @brief The Frame structure encapsulates information about the Frame within the network package. **/
  struct Frame frame;

};
typedef struct Truffle Truffle_t;

#endif