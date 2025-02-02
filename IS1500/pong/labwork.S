  # labwork.S
  # Written 2015-2017 by F Lundevall
  # Skeleton file for IS1200/IS1500 lab 1.
  # The macros PUSH and POP are in the public domain.
  # Please add your own code at the end of the file.
  # Modified by Pontus Curtsson pontuscu

  #
  # Please keep the two macros PUSH and POP unchanged
  #
.macro	PUSH reg
	addi	$sp,$sp,-4
	sw	\reg,0($sp)
.endm

.macro	POP reg
	lw	\reg,0($sp)
	addi	$sp,$sp,4
.endm
  #
  # Please add your own code below this line
  #

.text

.global enable_interrupt
enable_interrupt:
	ei
	jr		$ra
	nop

.global hexasc
  hexasc:
  	addi	$t0,$a0,0x30	# Add 0x30 to parameter since wanted ascii-values are in range 0x30-0x39 and 0x41-0x46
  	li	$t1,0x39	# This value is used since if given argument is > 0x39, you can assume that you need to adjust it for the ASCII-range of A-F
  	slt	$t2,$t1,$t0	# Checks if given argument is above upper bound $t1
  	bne	$0,$t2,chars	# Branches to chars if argument needs to be adjusted for range 0x41-0x46
  	j	return		# No adjustment needed, can return ascii-value directly
  	nop
  chars:
  	addi	$t0,$t0,0x7	# Adds 0x7 to adjust $t0. Since diff between ASCII range 0x39-0x41 is 0x7
  return:
  	add	$v0,$t0,$0
  	jr 	$ra
  	nop

.global delay
  delay:
  	li	$t0, 4711		# Constant for inner loop
  	addi	$a0, $a0, -1	# Adjust outer loop counter
  	beq	$0, $a0, endloop
  forl:
  	addi	$t0, $t0, -1	# Subtract 1 from inner loop counter
  	beq	$0, $t0, delay
  	nop
  	j forl
  	nop
  endloop:
  	jr	$ra
  	nop

.global time2string
time2string:
	li	$t1, 0x0	# Counter for loop
	PUSH	$ra		# Store correct return adress for time2string
	PUSH	$s0
	PUSH	$s1
	PUSH	$s2
	move	$s0, $a0	# Stores adress for timestring in $s0
loop:
	andi	$t0, $a1, 0xF	# 0xF is used to get the first digit out of the xx:xx time format stored in $a1
	PUSH	$t0		# Stores the digit on the stack
	srl	$a1, $a1, 4	# Shifts 4 steps right, to remove the first digit and get access to the second
	addi	$t1, $t1, 1	# Increments counter
	slti	$t2, $t1, 0x4	# Checks counter to see if finished
	bne	$0, $t2, loop	# If not done, loop back and do again for each 4 digits
	nop

	li	$s1, 0x0	# Counter for second loop
	li	$s2, 0x2	# Used to check if counter = 2
loophex:
	POP	$t0
	move 	$a0, $t0	# Moves current digit stored in $t0 to $a0 for hexasc
	jal	hexasc		# Converts $a0 into ascii-value and stores in $v0
	nop
	sb	$v0, ($s0)	# Stores $v0 at $s0 adress
	addi	$s1, $s1, 1	# Increments counter
	addiu	$s0, $s0, 1	# Adjusts memory position
	beq	$s2, $s1, colon	# Writes a ":" to memory, but only when counter is = 2
	nop
	slti	$t0, $s1, 0x5	# Checks if loop is finished
	bne	$0, $t0, loophex
	nop
	j	return1		# jumps to end of subroutine
	nop

colon:
	li	$t0, 0x3A	# 0x3A is value for a ":"
	sb	$t0, ($s0)
	addi	$s0, $s0, 1	# Specialcase add for memory positioning
	addi	$s1, $s1, 1	# Specialcase add for counter
	j	loophex
	nop

return1:
	li	$t0, 0x0	# 0x0 is ascii nullbyte
	sb	$t0, ($s0)	# Writes nullbyte to memory
	POP	$s2		# Restores all used non-temp registers
	POP	$s1
	POP	$s0
	POP	$ra
	jr $ra
	nop
