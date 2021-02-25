package com.ubirch

import com.fasterxml.jackson.databind.node.BinaryNode
import com.ubirch.protocol.ProtocolMessage
import com.ubirch.protocol.codec.{MsgPackProtocolDecoder, MsgPackProtocolEncoder}
import org.apache.commons.codec.binary.Hex

import java.util.Base64

object UPPFactory {
  private val decorder = MsgPackProtocolDecoder.getDecoder()
  private val encoder = MsgPackProtocolEncoder.getEncoder()

  def createNewUPP(msgPack: Array[Byte]): Array[Char] = {
    val protocolMessage = decorder.decode(msgPack)

    val newProtocolMessage = createNewProtocolMessage(protocolMessage)
    val newMsgpackByteArray = encoder.encode(newProtocolMessage)

    Hex.encodeHex(newMsgpackByteArray)
  }

  def createNewUPP(msgPack: String): Array[Char] = {
    val protocolMessage = decorder.decode(Hex.decodeHex(msgPack))

    val newProtocolMessage = createNewProtocolMessage(protocolMessage)
    val newMsgpackByteArray = encoder.encode(newProtocolMessage)

    Hex.encodeHex(newMsgpackByteArray)
  }

  private def createNewProtocolMessage(pm: ProtocolMessage): ProtocolMessage = {
    val newProcotolMessage = new ProtocolMessage()

    val CHAINEDMESSAGE = 35
    newProcotolMessage.setUUID(pm.getUUID)
    newProcotolMessage.setHint(pm.getHint)
    newProcotolMessage.setChain(pm.getChain)
    newProcotolMessage.setSigned(pm.getSigned)
    newProcotolMessage.setVersion(CHAINEDMESSAGE)
    newProcotolMessage.setSignature(pm.getSignature)

    val payloadHash = Hash.sha512.digest(pm.getPayload.asText.getBytes())
    newProcotolMessage.setPayload(BinaryNode.valueOf(Base64.getEncoder.encode(payloadHash)))
    newProcotolMessage
  }
}
