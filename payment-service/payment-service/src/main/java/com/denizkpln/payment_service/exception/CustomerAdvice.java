//package com.denizkpln.payment_service.exception;
//
//import com.google.protobuf.Any;
//import com.google.rpc.Code;
//import io.grpc.Metadata;
//import io.grpc.Status;
//import io.grpc.StatusRuntimeException;
//import io.grpc.protobuf.ProtoUtils;
//import io.grpc.protobuf.StatusProto;
//import net.devh.boot.grpc.server.advice.GrpcAdvice;
//import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;
//
//@GrpcAdvice
//public class CustomerAdvice {
//
//    @GrpcExceptionHandler(CustomErrorException.class)
//    public StatusRuntimeException handleCustomException(CustomErrorException exception){
//        com.google.rpc.Status status = com.google.rpc.Status.newBuilder()
//                .setCode(Code.NOT_FOUND_VALUE)
//                .addDetails(Any.pack(CustomError.newBuilder()
//                        .setMessage(exception.getMessage())
//                        .setErrorType("NOT_FOUND")
//                        .build())).build();
//
//        return StatusProto.toStatusRuntimeException(status);
//    }
//}