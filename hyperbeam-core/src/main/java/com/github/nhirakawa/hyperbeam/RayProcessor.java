package com.github.nhirakawa.hyperbeam;

import static com.github.nhirakawa.hyperbeam.util.MathUtils.rand;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.github.nhirakawa.hyperbeam.geometry.Ray;
import com.github.nhirakawa.hyperbeam.geometry.Vector3;
import com.github.nhirakawa.hyperbeam.shape.AxisAlignedBoundingBox;
import com.github.nhirakawa.hyperbeam.shape.BoundingVolumeHierarchyModel;
import com.github.nhirakawa.hyperbeam.shape.BoxModel;
import com.github.nhirakawa.hyperbeam.shape.ConstantMediumModel;
import com.github.nhirakawa.hyperbeam.shape.HitRecord;
import com.github.nhirakawa.hyperbeam.shape.MovingSphereModel;
import com.github.nhirakawa.hyperbeam.shape.ReverseNormalsModel;
import com.github.nhirakawa.hyperbeam.shape.SceneObject;
import com.github.nhirakawa.hyperbeam.shape.SceneObjectsList;
import com.github.nhirakawa.hyperbeam.shape.SphereModel;
import com.github.nhirakawa.hyperbeam.shape.XYRectangleModel;
import com.github.nhirakawa.hyperbeam.shape.XZRectangleModel;
import com.github.nhirakawa.hyperbeam.shape.YZRectangleModel;
import com.github.nhirakawa.hyperbeam.transform.TranslationModel;
import com.github.nhirakawa.hyperbeam.transform.YRotationModel;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class RayProcessor {

  // hit record

  public Optional<HitRecord> hit(SceneObject sceneObject, Ray ray, double tMin, double tMax) {
    if (sceneObject instanceof SphereModel) {
      return hitSphere((SphereModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof BoundingVolumeHierarchyModel) {
      return hitBoundingVolumeHierarchy((BoundingVolumeHierarchyModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof BoxModel) {
      return hitBox((BoxModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof SceneObjectsList) {
      return hitSceneObjectsList((SceneObjectsList) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof ConstantMediumModel) {
      return hitConstantMedium((ConstantMediumModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof MovingSphereModel) {
      return hitMovingSphere((MovingSphereModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof ReverseNormalsModel) {
      return hitReverseNormals((ReverseNormalsModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof XYRectangleModel) {
      return hitXYRectantle((XYRectangleModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof XZRectangleModel) {
      return hitXZRectangle((XZRectangleModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof YZRectangleModel) {
      return hitYZRectangle((YZRectangleModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof TranslationModel) {
      return hitTranslation((TranslationModel) sceneObject, ray, tMin, tMax);
    }

    if (sceneObject instanceof YRotationModel) {
      return hitYRotation((YRotationModel) sceneObject, ray, tMin, tMax);
    }

    throw new IllegalArgumentException("Can't hit unknown object - " + sceneObject);
  }

  private Optional<HitRecord> hitSphere(SphereModel sphere, Ray ray, double tMin, double tMax) {
    Vector3 oc = ray.getOrigin().subtract(sphere.getCenter());

    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (sphere.getRadius() * sphere.getRadius());

    double discriminant = (b * b) - (a * c);

    if (discriminant > 0) {
      double negativeTemp = (-b - Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      Range<Double> parameterRange = Range.open(tMin, tMax);

      if (parameterRange.contains(negativeTemp)) {
        Vector3 point = ray.getPointAtParameter(negativeTemp);
        Vector3 normal = point.subtract(sphere.getCenter()).scalarDivide(sphere.getRadius());

        Vector3 uvPoint = point.subtract(sphere.getCenter());

        double phi = Math.atan2(uvPoint.getZ(), uvPoint.getX());
        double theta = Math.asin(uvPoint.getY());

        double u = (1 - phi + Math.PI) / (2 * Math.PI);
        double v = (theta + (Math.PI / 2)) / Math.PI;

        return Optional.of(
            HitRecord.builder()
                .setT(negativeTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(sphere.getMaterial())
                .setU(u)
                .setV(v)
                .build()
        );
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(sphere.getCenter()).scalarDivide(sphere.getRadius());

        Vector3 uvPpoint = point.subtract(sphere.getCenter());

        double phi = Math.atan2(uvPpoint.getZ(), uvPpoint.getX());
        double theta = Math.asin(uvPpoint.getY());

        double u = (1 - phi + Math.PI) / (2 * Math.PI);
        double v = (theta + (Math.PI / 2)) / Math.PI;

        return Optional.of(
            HitRecord.builder()
                .setT(positiveTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(sphere.getMaterial())
                .setU(u)
                .setV(v)
                .build()
        );
      }
    }

    return Optional.empty();
  }

  private Optional<HitRecord> hitBoundingVolumeHierarchy(BoundingVolumeHierarchyModel boundingVolumeHierarchy, Ray ray, double tMin, double tMax) {
    if (getAxisAlignedBoundingBoxForBoundingVolumeHierarchy(boundingVolumeHierarchy).hit(ray, tMin, tMax)) {
      Optional<HitRecord> leftHitRecord = hit(boundingVolumeHierarchy.getLeft(), ray, tMin, tMax);
      Optional<HitRecord> rightHitRecord = hit(boundingVolumeHierarchy.getRight(), ray, tMin, tMax);

      if (leftHitRecord.isPresent() && rightHitRecord.isPresent()) {
        if (leftHitRecord.get().getT() < rightHitRecord.get().getT()) {
          return leftHitRecord;
        } else {
          return rightHitRecord;
        }
      } else if (leftHitRecord.isPresent()) {
        return leftHitRecord;
      } else if (rightHitRecord.isPresent()) {
        return rightHitRecord;
      } else {
        return Optional.empty();
      }
    } else {
      return Optional.empty();
    }
  }

  private Optional<HitRecord> hitBox(BoxModel box, Ray ray, double tMin, double tMax) {
    return hit(box.getSceneObjectsList(), ray, tMin, tMax);
  }

  private Optional<HitRecord> hitSceneObjectsList(SceneObjectsList sceneObjectsList, Ray ray, double tMin, double tMax) {
    Optional<HitRecord> tempRecord = Optional.empty();
    double closestSoFar = tMax;

    for (int i = 0; i < sceneObjectsList.getHittables().size(); i++) {
      Optional<HitRecord> maybeHitRecord = hit(sceneObjectsList.getHittables().get(i), ray, tMin, closestSoFar);
      if (maybeHitRecord.isPresent()) {
        tempRecord = maybeHitRecord;
        closestSoFar = maybeHitRecord.get().getT();
      }
    }

    return tempRecord;
  }

  private Optional<HitRecord> hitConstantMedium(ConstantMediumModel constantMedium, Ray ray, double tMin, double tMax) {
    Optional<HitRecord> hitRecord1 = hit(constantMedium.getSceneObject(), ray, -Double.MAX_VALUE, Double.MAX_VALUE);
    if (!hitRecord1.isPresent()) {
      return Optional.empty();
    }

    Optional<HitRecord> hitRecord2 = hit(constantMedium.getSceneObject(), ray, hitRecord1.get().getT() + 0.0001, Double.MAX_VALUE);
    if (!hitRecord2.isPresent()) {
      return Optional.empty();
    }

    double hitRecord1MaxT = hitRecord1.get().getT();
    if (hitRecord1MaxT < tMin) {
      hitRecord1MaxT = tMin;
    }

    double hitRecord2MinT = hitRecord2.get().getT();
    if (hitRecord2MinT > tMax) {
      hitRecord2MinT = tMax;
    }

    if (hitRecord1MaxT >= hitRecord2MinT) {
      return Optional.empty();
    }

    if (hitRecord1MaxT < 0) {
      hitRecord1MaxT = 0;
    }

    double distanceInsideBoundary = (hitRecord2MinT - hitRecord1MaxT) * ray.getDirection().getNorm();
    double hitDistance = (-1 / constantMedium.getDensity()) * Math.log(rand());

    if (hitDistance >= distanceInsideBoundary) {
      return Optional.empty();
    }

    double t = hitRecord1MaxT + (hitDistance / ray.getDirection().getNorm());
    Vector3 point = ray.getPointAtParameter(t);

    return Optional.of(
        HitRecord.builder()
            .setPoint(point)
            .setMaterial(constantMedium.getPhaseFunction())
            .setNormal(ConstantMediumModel.normal())
            .setT(t)
            .build()
    );
  }

  private Optional<HitRecord> hitMovingSphere(MovingSphereModel movingSphere, Ray ray, double tMin, double tMax) {
    Vector3 center = movingSphere.getCenter(ray.getTime());
    Vector3 oc = ray.getOrigin().subtract(center);

    double a = ray.getDirection().dotProduct(ray.getDirection());
    double b = oc.dotProduct(ray.getDirection());
    double c = oc.dotProduct(oc) - (movingSphere.getRadius() * movingSphere.getRadius());

    double discriminant = (b * b) - (a * c);

    if (discriminant > 0) {
      double negativeTemp = (-b - Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      Range<Double> parameterRange = Range.open(tMin, tMax);

      if (parameterRange.contains(negativeTemp)) {
        Vector3 point = ray.getPointAtParameter(negativeTemp);
        Vector3 normal = point.subtract(center).scalarDivide(movingSphere.getRadius());
        return Optional.of(
            HitRecord.builder()
                .setT(negativeTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(movingSphere.getMaterial())
                .build()
        );
      }

      double positiveTemp = (-b + Math.sqrt(discriminant)) / a; // TODO rename this awful variable
      if (parameterRange.contains(positiveTemp)) {
        Vector3 point = ray.getPointAtParameter(positiveTemp);
        Vector3 normal = point.subtract(center).scalarDivide(movingSphere.getRadius());
        return Optional.of(
            HitRecord.builder()
                .setT(positiveTemp)
                .setPoint(point)
                .setNormal(normal)
                .setMaterial(movingSphere.getMaterial())
                .build()
        );
      }
    }

    return Optional.empty();
  }

  private Optional<HitRecord> hitReverseNormals(ReverseNormalsModel reverseNormals, Ray ray, double tMin, double tMax) {
    return hit(reverseNormals.getSceneObject(), ray, tMin, tMax)
        .map(hitRecord -> hitRecord.withNormal(hitRecord.getNormal().negate()));
  }

  private Optional<HitRecord> hitXYRectantle(XYRectangleModel xyRectangle, Ray ray, double tMin, double tMax) {
    double t = (xyRectangle.getK() - ray.getOrigin().getZ()) / ray.getDirection().getZ();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double x = ray.getOrigin().getX() + (ray.getDirection().getX() * t);
    double y = ray.getOrigin().getY() + (ray.getDirection().getY() * t);

    if (x < xyRectangle.getX0() || x > xyRectangle.getX1() || y < xyRectangle.getY0() || y > xyRectangle.getY1()) {
      return Optional.empty();
    }

    double u = (x - xyRectangle.getX0()) / (xyRectangle.getX1() - xyRectangle.getX0());
    double v = (y - xyRectangle.getY0()) / (xyRectangle.getY1() - xyRectangle.getY0());

    Vector3 normal = Vector3.builder()
        .setX(0)
        .setY(0)
        .setZ(1)
        .build();

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setNormal(normal)
            .setMaterial(xyRectangle.getMaterial())
            .setT(t)
            .setU(u)
            .setV(v)
            .build()
    );
  }

  private Optional<HitRecord> hitXZRectangle(XZRectangleModel xzRectangle, Ray ray, double tMin, double tMax) {
    double t = (xzRectangle.getK() - ray.getOrigin().getY()) / ray.getDirection().getY();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double x = ray.getOrigin().getX() + (t * ray.getDirection().getX());
    double z = ray.getOrigin().getZ() + (t * ray.getDirection().getZ());

    if (x < xzRectangle.getX0() || x > xzRectangle.getX1() || z < xzRectangle.getZ0() || z > xzRectangle.getZ1()) {
      return Optional.empty();
    }

    double u = (x - xzRectangle.getX0()) / (xzRectangle.getX1() - xzRectangle.getX0());
    double v = (z - xzRectangle.getZ0()) / (xzRectangle.getZ1() - xzRectangle.getZ0());

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setT(t)
            .setU(u)
            .setV(v)
            .setNormal(xzRectangle.getNormal())
            .setMaterial(xzRectangle.getMaterial())
            .build()
    );
  }

  private Optional<HitRecord> hitYZRectangle(YZRectangleModel yzRectangle, Ray ray, double tMin, double tMax) {
    double t = (yzRectangle.getK() - ray.getOrigin().getX()) / ray.getDirection().getX();

    if (t < tMin || t > tMax) {
      return Optional.empty();
    }

    double y = ray.getOrigin().getY() + (t * ray.getDirection().getY());
    double z = ray.getOrigin().getZ() + (t * ray.getDirection().getZ());

    if (y < yzRectangle.getY0() || y > yzRectangle.getY1() || z < yzRectangle.getZ0() || z > yzRectangle.getZ1()) {
      return Optional.empty();
    }

    double u = (y - yzRectangle.getY0()) / (yzRectangle.getY1() - yzRectangle.getY0());
    double v = (z - yzRectangle.getZ0()) / (yzRectangle.getZ1() - yzRectangle.getZ0());

    return Optional.of(
        HitRecord.builder()
            .setPoint(ray.getPointAtParameter(t))
            .setMaterial(yzRectangle.getMaterial())
            .setT(t)
            .setU(u)
            .setV(v)
            .setNormal(yzRectangle.getNormal())
            .build()
    );
  }

  private Optional<HitRecord> hitTranslation(TranslationModel translation, Ray ray, double tMin, double tMax) {
    Ray offsetRay = ray.withOrigin(ray.getOrigin().subtract(translation.getOffset()));

    return hit(translation.getSceneObject(), offsetRay, tMin, tMax)
        .map(hitRecord -> hitRecord.withPoint(hitRecord.getPoint().add(translation.getOffset())));
  }

  private Optional<HitRecord> hitYRotation(YRotationModel yRotation, Ray ray, double tMin, double tMax) {
    Vector3 origin = ray.getOrigin();
    Vector3 direction = ray.getDirection();

    double newOriginX = (yRotation.getCosTheta() * origin.getX()) - (yRotation.getSinTheta() * origin.getZ());
    double newOriginZ = (yRotation.getSinTheta() * origin.getX()) + (yRotation.getCosTheta() * origin.getZ());

    double newDirectionX = (yRotation.getCosTheta() * direction.getX()) - (yRotation.getSinTheta() * direction.getZ());
    double newDirectionZ = (yRotation.getSinTheta() * direction.getX()) + (yRotation.getCosTheta() * direction.getZ());

    Vector3 newOrigin = Vector3.builder()
        .setX(newOriginX)
        .setY(origin.getY())
        .setZ(newOriginZ)
        .build();

    Vector3 newDirection = Vector3.builder()
        .setX(newDirectionX)
        .setY(direction.getY())
        .setZ(newDirectionZ)
        .build();

    Ray rotatedRay = Ray.builder()
        .from(ray)
        .setOrigin(newOrigin)
        .setDirection(newDirection)
        .build();

    Optional<HitRecord> maybeHitRecord = hit(yRotation.getSceneObject(), rotatedRay, tMin, tMax);
    if (!maybeHitRecord.isPresent()) {
      return Optional.empty();
    }

    HitRecord hitRecord = maybeHitRecord.get();
    Vector3 point = hitRecord.getPoint();
    Vector3 normal = hitRecord.getNormal();

    double newPointX = (yRotation.getCosTheta() * point.getX()) + (yRotation.getSinTheta() * point.getZ());
    double newPointZ = (yRotation.getCosTheta() * point.getZ()) - (yRotation.getSinTheta() * point.getX());

    double newNormalX = (yRotation.getCosTheta() * normal.getX()) + (yRotation.getSinTheta() * normal.getZ());
    double newNormalZ = (yRotation.getCosTheta() * normal.getZ()) - (yRotation.getSinTheta() * normal.getZ());

    Vector3 newPoint = Vector3.builder()
        .setX(newPointX)
        .setY(point.getY())
        .setZ(newPointZ)
        .build();

    Vector3 newNormal = Vector3.builder()
        .setX(newNormalX)
        .setY(normal.getY())
        .setZ(newNormalZ)
        .build();

    return Optional.of(
        HitRecord.builder()
            .from(hitRecord)
            .setPoint(newPoint)
            .setNormal(newNormal)
            .build()
    );
  }

  // bounding box

  public Optional<AxisAlignedBoundingBox> getBoundingBox(SceneObject sceneObject, double t0, double t1) {
    if (sceneObject instanceof BoundingVolumeHierarchyModel) {
      return getBoundingBoxForBoundingVolumeHierarcy((BoundingVolumeHierarchyModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof BoxModel) {
      return getBoundingBoxForBox((BoxModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof ConstantMediumModel) {
      return getBoundingBoxForConstantMedium((ConstantMediumModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof MovingSphereModel) {
      return getBoundingBoxForMovingSphere((MovingSphereModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof ReverseNormalsModel) {
      return getBoundingBoxForReverseNormals((ReverseNormalsModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof SceneObjectsList) {
      return getBoundingBoxForSceneObjectsList((SceneObjectsList) sceneObject, t0, t1);
    }

    if (sceneObject instanceof SphereModel) {
      return getBoundingBoxForSphere((SphereModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof XYRectangleModel) {
      return getBoundingBoxForXYRectangle((XYRectangleModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof XZRectangleModel) {
      return getBoundingBoxForXZRectangle((XZRectangleModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof YZRectangleModel) {
      return getBoundingBoxForYZRectangle((YZRectangleModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof TranslationModel) {
      return getBoundingBoxForTranslation((TranslationModel) sceneObject, t0, t1);
    }

    if (sceneObject instanceof YRotationModel) {
      return getBoundingBoxForYRotation((YRotationModel) sceneObject, t0, t1);
    }

    throw new IllegalArgumentException("Cannot get bounding box for " + sceneObject);
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForBoundingVolumeHierarcy(BoundingVolumeHierarchyModel boundingVolumeHierarchy, double t0, double t1) {
    return Optional.of(getAxisAlignedBoundingBoxForBoundingVolumeHierarchy(boundingVolumeHierarchy));
  }

  private AxisAlignedBoundingBox getAxisAlignedBoundingBoxForBoundingVolumeHierarchy(BoundingVolumeHierarchyModel boundingVolumeHierarchy) {
    Optional<AxisAlignedBoundingBox> leftBox =getBoundingBox(boundingVolumeHierarchy.getLeft(), boundingVolumeHierarchy.getTime0(), boundingVolumeHierarchy.getTime1());
    Optional<AxisAlignedBoundingBox> rightBox = getBoundingBox(boundingVolumeHierarchy.getRight(), boundingVolumeHierarchy.getTime0(), boundingVolumeHierarchy.getTime1());

    Preconditions.checkState(leftBox.isPresent(), "Could not get bounding box for %s", boundingVolumeHierarchy.getLeft());
    Preconditions.checkState(rightBox.isPresent(), "Could not get bounding box for %s", boundingVolumeHierarchy.getRight());

    return AxisAlignedBoundingBox.getSurroundingBox(leftBox.get(), rightBox.get());
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForBox(BoxModel box, double t0, double t1) {
    return Optional.of(box.getBoundingBox());
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForConstantMedium(ConstantMediumModel constantMedium, double t0, double t1) {
    return getBoundingBox(constantMedium.getSceneObject(), t0, t1);
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForMovingSphere(MovingSphereModel movingSphere, double t0, double t1) {
    Vector3 radiusVector = Vector3.builder()
        .setX(movingSphere.getRadius())
        .setY(movingSphere.getRadius())
        .setZ(movingSphere.getRadius())
        .build();

    AxisAlignedBoundingBox box0 = AxisAlignedBoundingBox.builder()
        .setMin(movingSphere.getCenter(t0).subtract(radiusVector))
        .setMax(movingSphere.getCenter(t0).add(radiusVector))
        .build();
    AxisAlignedBoundingBox box1 = AxisAlignedBoundingBox.builder()
        .setMin(movingSphere.getCenter(t1).subtract(radiusVector))
        .setMax(movingSphere.getCenter(t1).add(radiusVector))
        .build();

    return Optional.of(AxisAlignedBoundingBox.getSurroundingBox(box0, box1));
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForReverseNormals(ReverseNormalsModel reverseNormals, double t0, double t1) {
    return getBoundingBox(reverseNormals.getSceneObject(), t0, t1);
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForSceneObjectsList(SceneObjectsList sceneObjectsList, double t0, double t1) {
    if (sceneObjectsList.getHittables().isEmpty()) {
      return Optional.empty();
    }

    Optional<AxisAlignedBoundingBox> current = getBoundingBox(sceneObjectsList.getHittables().get(0), t0, t1);
    if (!current.isPresent()) {
      return Optional.empty();
    }

    for (int i = 0; i < sceneObjectsList.getHittables().size(); i++) {
      Optional<AxisAlignedBoundingBox> temp = getBoundingBox(sceneObjectsList.getHittables().get(i), t0, t1);
      if (!temp.isPresent()) {
        return Optional.empty();
      }

      current = Optional.of(AxisAlignedBoundingBox.getSurroundingBox(current.get(), temp.get()));
    }

    return current;
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForSphere(SphereModel sphere, double t0, double t1) {
    return Optional.of(sphere.getAxisAlignedBoundingBox());
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForXYRectangle(XYRectangleModel xyRectangle, double t0, double t1) {
    return xyRectangle.getBoundingBox();
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForXZRectangle(XZRectangleModel xzRectangle, double t0, double t1) {
    return Optional.of(xzRectangle.getBoundingBox());
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForYZRectangle(YZRectangleModel yzRectangle, double t0, double t1) {
    return Optional.of(yzRectangle.getBoundingBox());
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForTranslation(TranslationModel translation, double t0, double t1) {
    return getBoundingBox(translation.getSceneObject(), t0, t1)
        .map(translation::getOffsetAxisAlignedBoundingBox);
  }

  private Optional<AxisAlignedBoundingBox> getBoundingBoxForYRotation(YRotationModel yRotation, double t0, double t1) {
    Optional<AxisAlignedBoundingBox> maybeBoundingBox = getBoundingBox(yRotation.getSceneObject(), 0, 1);
    if (!maybeBoundingBox.isPresent()) {
      return Optional.empty();
    }

    AxisAlignedBoundingBox boundingBox = maybeBoundingBox.get();

    Vector3 min = Vector3.max();
    Vector3 max = Vector3.min();

    Set<Integer> integers = ImmutableSet.of(0, 1);
    Set<List<Integer>> products = Sets.cartesianProduct(integers, integers, integers);

    for (List<Integer> product : products) {
      Preconditions.checkState(product.size() == 3);

      int i = product.get(0);
      int j = product.get(1);
      int k = product.get(2);

      double x = (i * boundingBox.getMax().getX()) + ((1 - i) * boundingBox.getMin().getX());
      double y = (j * boundingBox.getMax().getY()) + ((1 - j) * boundingBox.getMin().getY());
      double z = (k * boundingBox.getMax().getZ()) + ((1 - k) * boundingBox.getMin().getZ());

      double newX = (yRotation.getCosTheta() * x) + (yRotation.getSinTheta() * z);
      double newZ = (yRotation.getCosTheta() * z) - (yRotation.getSinTheta() * x);

      Vector3 tester = Vector3.builder()
          .setX(newX)
          .setY(y)
          .setZ(newZ)
          .build();

      double maxX = Math.max(tester.getX(), max.getX());
      double maxY = Math.max(tester.getY(), max.getY());
      double maxZ = Math.max(tester.getZ(), max.getZ());

      max = Vector3.builder()
          .setX(maxX)
          .setY(maxY)
          .setZ(maxZ)
          .build();

      double minX = Math.min(tester.getX(), min.getX());
      double minY = Math.min(tester.getY(), min.getY());
      double minZ = Math.min(tester.getZ(), min.getZ());

      min = Vector3.builder()
          .setX(minX)
          .setY(minY)
          .setZ(minZ)
          .build();
    }

    return Optional.of(
        AxisAlignedBoundingBox.builder()
            .setMin(min)
            .setMax(max)
            .build()
    );
  }

}


